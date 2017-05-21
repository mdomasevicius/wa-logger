package lt.platform.lunar

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.RequestEntity
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

import static org.springframework.http.HttpMethod.GET

@Component
class RestClient {

    @Autowired
    TestRestTemplate restTemplate

    ResponseEntity<Map> post(String url, Object body) {
        return restTemplate.postForEntity(url, new HttpEntity(body), LinkedHashMap)
    }

    ResponseEntity<Map> get(String url) {
        def entity = new RequestEntity(GET, new URI(url))
        return restTemplate.exchange(entity, LinkedHashMap)
    }

    static String createdResourcePath(ResponseEntity response) {
        def location = response.headers.Location.find()
        assert location
        return new URL(location).path
    }
}
