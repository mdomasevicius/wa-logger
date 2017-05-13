package lt.lunar.platform

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.RequestEntity
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap

import static org.springframework.http.HttpMethod.*

@Component
class RestClient {

    @Autowired
    TestRestTemplate restTemplate

    def <T> ResponseEntity<T> post(String url, Object body, Map<String, String> headers = [:]) {
        MultiValueMap<String, String> headerMap = new LinkedMultiValueMap<>()
        headers.each {
            headerMap.add(it.key, it.value)
        }
        return restTemplate.postForEntity(url, new HttpEntity(body, headerMap), Object)
    }

     def <T> ResponseEntity<T> post(String url, Object request, Class<T> responseType) {
        return restTemplate.postForEntity(url, request, responseType)
    }

    ResponseEntity<Map> get(String url, Map<String, String> headers = [:]) {
        MultiValueMap<String, String> headerMap = new LinkedMultiValueMap<>()
        headers.each {
            headerMap.add(it.key, it.value)
        }
        def entity = new RequestEntity(headerMap, GET, new URI(url))
        return restTemplate.exchange(entity, new ParameterizedTypeReference<Map>() {})
    }

    static String createdResourcePath(ResponseEntity response) {
        def location = response.headers.Location.find()
        assert location
        return new URL(location).path
    }
}
