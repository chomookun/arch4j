package org.oopscraft.arch4j.web.sample;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.sample.Sample;
import org.oopscraft.arch4j.core.sample.SampleSearch;
import org.oopscraft.arch4j.core.sample.SampleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("sample")
@RequiredArgsConstructor
public class SampleController {

    private final SampleService sampleService;

    @GetMapping
    public ModelAndView index() {
        return new ModelAndView("sample.html");
    }

    @PostMapping("save-sample")
    @ResponseBody
    public Sample saveSample(@RequestBody Sample sample) {
        return sampleService.saveSample(sample);
    }

    /**
     * get sample
     * @param sampleId sample id
     * @return sample
     */
    @GetMapping("get-sample")
    @ResponseBody
    public Sample getSample(@RequestParam("sampleId") String sampleId) {
        return sampleService.getSample(sampleId)
                .orElseThrow();
    }

    /**
     * delete sample
     * @param sampleId sample id
     */
    @GetMapping("delete-sample")
    @ResponseBody
    public void deleteSample(@RequestParam("sampleId") String sampleId) {
        sampleService.deleteSample(sampleId);
    }

    /**
     * search samples
     * @param sampleSearch search condition
     * @param daoType dao type
     * @param pageable pageable
     * @return sample page
     */
    @GetMapping("get-samples")
    @ResponseBody
    public Page<Sample> getSamples(
            SampleSearch sampleSearch,
            @RequestParam(value = "daoType", required = false) String daoType,
            Pageable pageable
    ) {
        if("MYBATIS".equalsIgnoreCase(daoType)){
            return sampleService.getSamplesByJpa(sampleSearch, pageable);
        }
        if("QUERYDSL".equalsIgnoreCase(daoType)){
            return sampleService.getSamplesByQuerydsl(sampleSearch, pageable);
        }
        return sampleService.getSamplesByJpa(sampleSearch, pageable);
    }


}
