package com.bmps.difftool.rest;

import com.bmps.difftool.domain.ComparatorType;
import com.bmps.difftool.domain.DiffObject;
import com.bmps.difftool.domain.DiffObjectComparatorService;
import com.bmps.difftool.domain.DiffObjectRepository;
import com.bmps.difftool.exception.ClientException;
import com.github.difflib.algorithm.DiffException;
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
    private final DiffObjectComparatorService diffObjectComparatorService;

    public DiffController(DiffObjectRepository diffObjectRepository, DiffObjectComparatorService diffObjectComparatorService) {
        this.diffObjectRepository = diffObjectRepository;
        this.diffObjectComparatorService = diffObjectComparatorService;
    }

    @PostMapping(value = "/api/v1/diff/{id}/left", produces = {"application/json"}, consumes = {"application/octet-stream"})
    ResponseEntity<Void> persistLeftContent(@RequestBody byte[] leftObject, @PathVariable("id") UUID id) {

        diffObjectRepository.createOrUpdate(new DiffObject().withId(id.toString()).withLeft(leftObject));

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping(value = "/api/v1/diff/{id}/right", consumes = {"application/octet-stream"})
    ResponseEntity<Void> persistRightContent(@RequestBody byte[] rightObject, @PathVariable("id") UUID id) {

        diffObjectRepository.createOrUpdate(new DiffObject().withId(id.toString()).withRight(rightObject));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping(value = "/api/v1/diff/{id}", produces = {"application/json"})
    ResponseEntity<DiffOperationResponse> compareObjects(@PathVariable("id") UUID id,
                                                         @RequestParam(value = "diffType", required = false, defaultValue = "MYER") ComparatorType diffType)
            throws ClientException, IOException, DiffException {

        return ResponseEntity.status(HttpStatus.OK).body(diffObjectComparatorService.compare(id, diffType));
    }
}
