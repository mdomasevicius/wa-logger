package lt.platform.lunar.logger

import lt.platform.lunar.App
import lt.platform.lunar.RestClient
import lt.platform.lunar.logger.celebrities.CelebrityResource
import lt.platform.lunar.logger.key.RemoteKeyResource
import lt.platform.lunar.logger.url.CrawlURLResource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import static lt.platform.lunar.RestClient.createdResourcePath

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = App)
class CrawlURLRestSpec extends Specification {

    @Autowired
    RestClient restClient

    def 'can create urls'() {
        when:
            def createResponse = restClient.post('/api/url', new CrawlURLResource(url: 'http://test'))
        then:
            createResponse.statusCode.value() == 201
            createResponse.headers.Location

        when:
            def getResponse = restClient.get(createdResourcePath(createResponse))
        then:
            getResponse.statusCode.is2xxSuccessful()
            getResponse.body.id
            getResponse.body.url == 'http://test'
    }

    def 'crawler id is required'() {
        when:
            def response = restClient.post('/api/url', new CrawlURLResource())
        then:
            response.statusCode.is4xxClientError()
    }

    def 'crawler url is required'() {
        when:
            def response = restClient.post('/api/url', new CrawlURLResource())
        then:
            response.statusCode.is4xxClientError()
    }

    def 'url is required'() {
        when:
            def response = restClient.post('/api/url', new CrawlURLResource())
        then:
            response.statusCode.is4xxClientError()
    }

    def 'url is not processed if no remote key or celebrity was registered'() {
        given:
            def urlCreateResponse = restClient.post('/api/url', new CrawlURLResource(url: 'http://unfinished'))

        when:
            def response = restClient.get('/api/url?unfinishedOnly=true')
        then:
            response.statusCode.is2xxSuccessful()
            response.body.entries.find { it.url == 'http://unfinished' }

        when:
            restClient.post(
                "${createdResourcePath(urlCreateResponse)}/remote-key",
                new RemoteKeyResource('test'))
        then:
            restClient.get('/api/url?unfinishedOnly=true').body
                .entries.find { it.url == 'http://unfinished' }

        when:
            restClient.post(
                "${createdResourcePath(urlCreateResponse)}/celebrities",
                new CelebrityResource(
                    firstName: 'Bruce',
                    lastName: 'Wayne',
                    address: 'Arkham St. 100'
                ))
        then:
            !restClient.get('/api/url?unfinishedOnly=true').body
                .entries.find { it.url == 'http://unfinished' }
    }

}
