package br.com.netlit.accounts.domain.highlight;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/highlights")
public class HighlightResource {

  @GetMapping
  public ResponseEntity findAll() {
    return ResponseEntity.ok()
        .header("Content-Type", "application/hal+json")
        .body("{\"_embedded\":{\"books\":[{\"title\":\"Oh, the Places You'll Go!\",\"cover_link\":\"https://images-na.ssl-images-amazon.com/images/I/81a5KHEkwQL._AC_SR201,266_.jpg\",\"_links\":{\"self\":{\"href\":\"https://netlit.com/v1/books/2a4aefc6-7360-4238-97a5-d5ca8732dd7c\"},\"book\":{\"href\":\"https://netlit.com/v1/books/2a4aefc6-7360-4238-97a5-d5ca8732dd7c\"}}},{\"title\":\"The Very Hungry Caterpillar\",\"cover_link\":\"https://images-na.ssl-images-amazon.com/images/I/81TGQ1cTcrL._AC_SR201,266_.jpg\",\"_links\":{\"self\":{\"href\":\"https://netlit.com/v1/books/1ac91a7d-c529-4282-be0e-415e7986d7b9\"},\"book\":{\"href\":\"https://netlit.com/v1/books/1ac91a7d-c529-4282-be0e-415e7986d7b9\"}}},{\"title\":\"Goodnight Moon\",\"cover_link\":\"https://images-na.ssl-images-amazon.com/images/I/51%2BmV1XUUQL._AC_SR201,266_.jpg\",\"_links\":{\"self\":{\"href\":\"https://netlit.com/v1/books/23da489d-c1f5-4df7-8711-128502afbccb\"},\"book\":{\"href\":\"https://netlit.com/v1/books/23da489d-c1f5-4df7-8711-128502afbccb\"}}},{\"title\":\"The Giving Tree\",\"cover_link\":\"https://images-na.ssl-images-amazon.com/images/I/71wiGMKadmL._AC_SR201,266_.jpg\",\"_links\":{\"self\":{\"href\":\"https://netlit.com/v1/books/199bd06b-8252-40f3-8895-19da3812ae88\"},\"book\":{\"href\":\"https://netlit.com/v1/books/199bd06b-8252-40f3-8895-19da3812ae88\"}}}]},\"_links\":{\"self\":{\"href\":\"https://netlit.com/v1/highlights\"}}}");
  }
}

