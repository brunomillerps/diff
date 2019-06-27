package com.bmps.difftool.rest;

import com.bmps.difftool.domain.DiffObject;
import com.bmps.difftool.domain.DiffObjectComparator;
import com.bmps.difftool.domain.DiffObjectRepository;
import com.bmps.difftool.exception.ClientException;
import com.github.fge.jsonpatch.JsonPatchException;
import io.swagger.annotations.Api;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@Api(value = "v1", description = "APIs REST to diff 2 previously reported content", basePath = "/api")
@RestController
public class DiffController {

    private final DiffObjectRepository diffObjectRepository;
    private final DiffObjectComparator diffObjectComparator;

    public DiffController(DiffObjectRepository diffObjectRepository, DiffObjectComparator diffObjectComparator) {
        this.diffObjectRepository = diffObjectRepository;
        this.diffObjectComparator = diffObjectComparator;
    }

    @RequestMapping(value = "/v1/diff/{id}/left",
            produces = {"application/json"},
            consumes = {"application/octet-stream"},
            method = RequestMethod.POST)
    ResponseEntity<Void> persistLeftContent(@RequestBody byte[] leftObject,
                                            @PathVariable("id") UUID id) {

        diffObjectRepository.createOrUpdate(new DiffObject().withId(id.toString()).withLeft(leftObject));

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @RequestMapping(value = "/v1/diff/{id}/right",
            consumes = {"application/octet-stream"},
            method = RequestMethod.POST)
    ResponseEntity<Void> persistRightContent(@RequestBody byte[] rightObject,
                                             @PathVariable("id") UUID id) {

        diffObjectRepository.createOrUpdate(new DiffObject().withId(id.toString()).withRight(rightObject));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @RequestMapping(value = "/v1/diff/{id}",
            produces = {"application/json"},
            method = RequestMethod.POST)
    ResponseEntity<DiffOperationResponse> compareObjects(@PathVariable("id") UUID id) throws ClientException, IOException, JsonPatchException {

        DiffOperationResponse response = new DiffOperationResponse();
        return ResponseEntity.status(HttpStatus.OK).body(diffObjectComparator.compare(id));
    }
}
