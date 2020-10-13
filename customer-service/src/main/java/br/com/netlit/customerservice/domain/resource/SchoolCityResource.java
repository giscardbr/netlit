package br.com.netlit.customerservice.domain.resource;

import br.com.netlit.customerservice.domain.resource.data.SchoolCityRequest;
import br.com.netlit.customerservice.domain.service.SchoolCityService;
import javax.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping()
public class SchoolCityResource {


  private final SchoolCityService schoolCityService;

  public SchoolCityResource(
      SchoolCityService schoolCityService) {
    this.schoolCityService = schoolCityService;
  }

  @PostMapping(value = "/school-city")
  public void postSchoolCity(@Valid @RequestBody SchoolCityRequest schoolCityRequest) {
    schoolCityService.createdEmail(schoolCityRequest);
  }


}
