package lt.lunar.platform.logger

import lt.lunar.platform.App
import lt.lunar.platform.RestClient
import lt.lunar.platform.logger.url.CrawlURLResource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import static lt.lunar.platform.RestClient.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = [App])
class CrawlURLRestSpec extends Specification {

    @Autowired
    RestClient restClient

    def 'can create urls'() {
        when:
            def createResponse = restClient.post('/api/url/', new CrawlURLResource(url: 'http://test'), ['Crawler-Id': '1'])
        then:
            createResponse.statusCode.value() == 201
            createResponse.headers.Location

        when:
            def getResponse = restClient.get(createdResourcePath(createResponse), ['Crawler-Id': '1'])
        then:
            getResponse.statusCode.is2xxSuccessful()
            getResponse.body.url == 'http://test'
    }

    def 'crawler id is required'() {
        when:
            def response = restClient.post('/api/url/', new CrawlURLResource())
        then:
            response.statusCode.is4xxClientError()
    }

    def 'crawler url is required'() {
        when:
            def response = restClient.post('/api/url/', new CrawlURLResource(), ['Crawler-Id': '1a'])
        then:
            response.statusCode.is4xxClientError()
    }

    def 'url is required'() {
        when:
            def response = restClient.post('/api/url/', new CrawlURLResource(), ['Crawler-Id': '1'])
        then:
            response.statusCode.is4xxClientError()
    }

    def 'crawlers may only access their own repo'() {
        given:
            def crawler1UrlLocation = createdResourcePath(
                restClient.post('/api/url/', new CrawlURLResource(url: 'http://aaaa1'), ['Crawler-Id': '11']))
        when:
            def response = restClient.get(crawler1UrlLocation, ['Crawler-Id': '99'])
        then:
            response.statusCode.is4xxClientError()
    }

}
