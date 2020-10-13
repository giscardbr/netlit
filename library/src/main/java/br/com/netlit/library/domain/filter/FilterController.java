package br.com.netlit.library.domain.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/filter")
@RestController
@RequiredArgsConstructor
public class FilterController {
    private final FilterResource filterResource;

    @GetMapping
    public Filter getFilters(){
        return filterResource.getFilters();
    }
}
