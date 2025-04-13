package org.chomookun.arch4j.web.view.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.info.InfoEndpoint;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@Controller
@RequestMapping("admin/monitor")
@PreAuthorize("hasAuthority('admin.monitor')")
@RequiredArgsConstructor
@Slf4j
public class MonitorController {

    private final InfoEndpoint infoEndpoint;

    private final MetricsEndpoint metricsEndpoint;

    private final MonitorScheduler monitorCollector;

    @GetMapping
    public ModelAndView index() {
        return new ModelAndView("admin/monitor.html");
    }

    @GetMapping("get-info")
    @ResponseBody
    public Map<String,Object> getInfo() {
        return infoEndpoint.info();
    }

    @GetMapping("get-cpu")
    @ResponseBody
    public List<Map<String,Object>> getCpu() {
        return monitorCollector.getCpu();
    }

    @GetMapping("get-memory")
    @ResponseBody
    public List<Map<String,Object>> getMemory() {
        return monitorCollector.getMemory();
    }

    @GetMapping("get-disk")
    @ResponseBody
    public List<Map<String,Object>> getDisk() {
        return monitorCollector.getDisk();
    }

    @GetMapping("get-server")
    @ResponseBody
    public List<Map<String,Object>> getServer() {
        return monitorCollector.getServer();
    }

    @GetMapping("get-datasource")
    @ResponseBody
    public List<Map<String,Object>> getDatasource() {
        return monitorCollector.getDatasource();
    }

    @GetMapping("get-requests")
    @ResponseBody
    public List<Map<String,Object>> getRequests() {
        List<Map<String,Object>> requests = new ArrayList<>();
        MetricsEndpoint.MetricDescriptor metricDescriptor = metricsEndpoint.metric("http.server.requests", null);
        // uris
        Set<String> uris = metricDescriptor.getAvailableTags().stream()
                .filter(availableTag -> "uri".equals(availableTag.getTag()))
                .findFirst()
                .map(MetricsEndpoint.AvailableTag::getValues)
                .orElse(Set.of());
        // methods
        Set<String> methods = metricDescriptor.getAvailableTags().stream()
                .filter(availableTag -> "method".equals(availableTag.getTag()))
                .findFirst()
                .map(MetricsEndpoint.AvailableTag::getValues)
                .orElse(Set.of());
        for (String uri : uris) {
            for (String method : methods) {
                Map<String,Object> request = new LinkedHashMap<>();
                List<String> tags = List.of("uri:" + uri, "method:" + method);
                MetricsEndpoint.MetricDescriptor uriMetricDescriptor = metricsEndpoint.metric("http.server.requests", tags);
                if (uriMetricDescriptor == null) {
                    continue;
                }
                List<MetricsEndpoint.Sample> samples = uriMetricDescriptor.getMeasurements();
                double totalCount = 0D;
                double totalTime = 0D;
                double averageTime = 0D;
                double maxTime = 0D;
                double successCount= 0D;
                double clientErrorCount = 0D;
                double serverErrorCount = 0D;
                for (MetricsEndpoint.Sample sample : samples) {
                    // 여려 건이 존재 할수 있다고 함으로 누적,Max 로 계산
                    switch (sample.getStatistic()) {
                        case COUNT -> totalCount += sample.getValue();
                        case TOTAL_TIME -> totalTime += sample.getValue();
                        case MAX -> maxTime = Math.max(maxTime, sample.getValue());
                    }
                }
                averageTime = totalCount == 0 ? 0 : totalTime / totalCount;
                // calculates status
                Optional<MetricsEndpoint.AvailableTag> statusTag = uriMetricDescriptor.getAvailableTags().stream()
                        .filter(tag -> "status".equals(tag.getTag()))
                        .findFirst();
                if (statusTag.isPresent()) {
                    for (String status : statusTag.get().getValues()) {
                        double statusCount = metricsEndpoint.metric("http.server.requests", List.of("uri:" + uri, "status:" + status))
                                .getMeasurements().stream()
                                .filter(m -> "COUNT".equals(m.getStatistic().name()))
                                .mapToDouble(MetricsEndpoint.Sample::getValue)
                                .sum();
                        int statusCode = Integer.parseInt(status);
                        if (statusCode >= 200 && statusCode < 300) {
                            successCount += statusCount;
                        } else if (statusCode >= 400 && statusCode < 500) {
                            clientErrorCount += statusCount;
                        } else if (statusCode >= 500) {
                            serverErrorCount += statusCount;
                        }
                    }
                }
                request.put("uri", uri);
                request.put("method", method);
                request.put("totalCount", totalCount);
                request.put("successCount", successCount);
                request.put("clientErrorCount", clientErrorCount);
                request.put("serverErrorCount", serverErrorCount);
                request.put("averageTime", Math.floor(averageTime * 1000) / 1000);
                request.put("maxTime", Math.floor(maxTime * 1000) / 1000);
                requests.add(request);
            }
        }
        // sort
        requests.sort(Comparator
                .comparing((Map<String,Object> request) -> (double) request.get("serverErrorCount")).reversed()
                .thenComparing((Map<String,Object> request) -> (double) request.get("averageTime")).reversed());
        // returns
        return requests;
    }

}
