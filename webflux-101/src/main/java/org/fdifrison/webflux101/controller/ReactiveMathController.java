package org.fdifrison.webflux101.controller;

import org.fdifrison.webflux101.dto.MultiplyRequest;
import org.fdifrison.webflux101.dto.Response;
import org.fdifrison.webflux101.service.ReactiveMathService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("r-math")
public class ReactiveMathController {

    // The subscriber will be the browser, hence we don't need to call subscribe on the publisher but simply return it

    private final ReactiveMathService service;

    public ReactiveMathController(ReactiveMathService service) {
        this.service = service;
    }

    @GetMapping("/square/{input}")
    public Mono<Response> findSquare(@PathVariable int input) {
        return service.findSquare(input);
    }

    // we have to wait for the process to finish consuming data,
    // but we can still cancel the subscription during the processing
    @GetMapping("/table/{input}")
    public Flux<Response> findMultiplicationTable(@PathVariable int input) {
        // AbstractJackson2Encoder:
        // this is the class responsible to collect the data into an array if no "produces"
        // option is provided in the getMapping.
        // On the onComplete signal, the encoder will wrap the items into an array and serve it to the browser
        return this.service.findMultiplicationTable(input);
    }

    // as a stream, we send values to the browser as soon as they are ready
    @GetMapping(value = "/table/{input}/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Response> findMultiplicationTableStream(@PathVariable int input) {
        return this.service.findMultiplicationTable(input);
    }

    // We can have both Mono<MultiplyRequest> or MultiplyRequest as body parameters;
    // This will depend on the fact that the body object might be big and need to be served asynchronously
    @PostMapping("multiply")
    public Mono<Response> multiply(@RequestBody Mono<MultiplyRequest> dto) {
        return this.service.multiply(dto);
    }

}