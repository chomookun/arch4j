package org.chomookun.arch4j.web.common.monitor;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@ConditionalOnProperty(prefix = "web.admin", name = "enabled", havingValue = "true", matchIfMissing = false)
@Lazy(false)
@RequiredArgsConstructor
@Getter
@Slf4j
public class MonitorScheduler {

    private final MetricsEndpoint metricsEndpoint;

    private final List<Map<String,Object>> cpu = new CopyOnWriteArrayList<>();

    private final List<Map<String,Object>> memory = new CopyOnWriteArrayList<>();

    private final List<Map<String,Object>> disk = new CopyOnWriteArrayList<>();

    private final List<Map<String,Object>> server = new CopyOnWriteArrayList<>();

    private final List<Map<String,Object>> datasource = new CopyOnWriteArrayList<>();

    private Double getMetricValue (String name) {
        try {
            MetricsEndpoint.MetricDescriptor metricDescriptor = metricsEndpoint.metric(name, null);
            List<MetricsEndpoint.Sample> samples = metricDescriptor.getMeasurements();
            return samples.get(0).getValue();
        } catch (Exception e) {
            log.warn("Failed to get metric value[{}]:{}", name, e.getMessage());
            return 0.0;
        }
    }

    @Scheduled(fixedDelay = 10 * 1_000)
    public void collectCpu() {
        Map<String,Object> cpu = new LinkedHashMap<>();
        cpu.put("time", System.currentTimeMillis());
        Double cpuUsage = getMetricValue("system.cpu.usage") * 100;
        cpu.put("usage", cpuUsage);
        if(this.cpu.size() >= 100) {
            this.cpu.remove(0);
        }
        this.cpu.add(cpu);
    }

    @Scheduled(fixedDelay = 10 * 1_000)
    public void collectMemory() {
        Map<String,Object> memory = new LinkedHashMap<>();
        memory.put("time", System.currentTimeMillis());
        Double max = getMetricValue("jvm.memory.max")/1024/1024;
        Double used = getMetricValue("jvm.memory.used")/1024/1024;
        memory.put("max", max);
        memory.put("used", used);
        if(this.memory.size() >= 100) {
            this.memory.remove(0);
        }
        this.memory.add(memory);
    }

    @Scheduled(fixedDelay = 10 * 1_000)
    public void collectDisk() {
        Map<String,Object> disk = new LinkedHashMap<>();
        disk.put("time", System.currentTimeMillis());
        Double total = getMetricValue("disk.total")/1014/1024/1024;
        Double free = getMetricValue("disk.free")/1024/1024/1024;
        Double used = total - free;
        disk.put("total", total);
        disk.put("used", used);
        disk.put("free", free);
        if(this.disk.size() >= 100) {
            this.disk.remove(0);
        }
        this.disk.add(disk);
    }

    @Scheduled(fixedDelay = 10 * 1_000)
    public void collectServer() {
        Map<String,Object> datasource = new LinkedHashMap<>();
        datasource.put("time", System.currentTimeMillis());
        Double max = getMetricValue("tomcat.threads.config.max");
        Double current = getMetricValue("tomcat.threads.current");
        Double busy = getMetricValue("tomcat.threads.busy");
        datasource.put("max", max);
        datasource.put("current", current);
        datasource.put("busy", busy);
        if(this.server.size() >= 100) {
            this.server.remove(0);
        }
        this.server.add(datasource);
    }

    @Scheduled(fixedDelay = 10 * 1_000)
    public void collectDatasource() {
        Map<String,Object> datasource = new LinkedHashMap<>();
        datasource.put("time", System.currentTimeMillis());
        Double max = getMetricValue("jdbc.connections.max");
        Double min = getMetricValue("jdbc.connections.min");
        Double active = getMetricValue("jdbc.connections.active");
        Double idle = getMetricValue("jdbc.connections.idle");
        datasource.put("max", max);
        datasource.put("min", min);
        datasource.put("active", active);
        datasource.put("idle", idle);
        if(this.datasource.size() >= 100) {
            this.datasource.remove(0);
        }
        this.datasource.add(datasource);
    }

}
