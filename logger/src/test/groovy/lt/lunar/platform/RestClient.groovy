package lt.lunar.platform

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.RequestEntity
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

import static org.springframework.http.HttpMethod.GET

@Component
class RestClient {

    @Autowired
    TestRestTemplate restTemplate

    def <T> ResponseEntity<T> post(String url, Object body) {
        return restTemplate.postForEntity(url, new HttpEntity(body), Object)
    }

     def <T> ResponseEntity<T> post(String url, Object request, Class<T> responseType) {
        return restTemplate.postForEntity(url, request, responseType)
    }

    ResponseEntity<Map> get(String url) {
        def entity = new RequestEntity(GET, new URI(url))
        return restTemplate.exchange(entity, new ParameterizedTypeReference<Map>() {})
    }

    static String createdResourcePath(ResponseEntity response) {
        def location = response.headers.Location.find()
        assert location
        return new URL(location).path
    }
}
