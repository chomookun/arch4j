package org.chomookun.arch4j.web.view.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.info.InfoEndpoint;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("admin/monitor")
@PreAuthorize("hasAuthority('admin.monitor')")
@RequiredArgsConstructor
@Slf4j
public class MonitorController {

    private final InfoEndpoint infoEndpoint;

    private final MonitorCollector monitorScheduler;

    @GetMapping
    public ModelAndView monitor() {
        return new ModelAndView("admin/monitor.html");
    }

    /**
     * Returns application info
     * @return application info
     */
    @GetMapping("get-info")
    @ResponseBody
    public Map<String,Object> getInfo() {
        return infoEndpoint.info();
    }

    /**
     * Returns cpu usage
     * @return cpu usage
     */
    @GetMapping("get-cpu")
    @ResponseBody
    public List<Map<String,Object>> getCpu() {
        return monitorScheduler.getCpu();
    }

    /**
     * Returns memory usage
     * @return memory usage
     */
    @GetMapping("get-memory")
    @ResponseBody
    public List<Map<String,Object>> getMemory() {
        return monitorScheduler.getMemory();
    }

    /**
     * Returns disk usage
     * @return disk usage
     */
    @GetMapping("get-disk")
    @ResponseBody
    public List<Map<String,Object>> getDisk() {
        return monitorScheduler.getDisk();
    }

    /**
     * Returns server info
     */
    @GetMapping("get-server")
    @ResponseBody
    public void getServer() {
        monitorScheduler.collectServer();
    }

    /**
     * Returns datasource usage
     * @return datasource usage
     */
    @GetMapping("get-datasource")
    @ResponseBody
    public List<Map<String,Object>> getDatasource() {
        return monitorScheduler.getDatasource();
    }

}
