package com.jeewaloka.digital.jeewalokadigital.controller;

import com.jeewaloka.digital.jeewalokadigital.dto.BillDTO;
import com.jeewaloka.digital.jeewalokadigital.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/Bill")
public class BillController {
    @Autowired
    private BillService billService;
    @GetMapping("/issueDate/{issueDate}")
    public ResponseEntity<List<BillDTO>> findByRole(@PathVariable LocalDateTime issueDate) {
        List<BillDTO> BillDTOS = billService.findByDate(issueDate);
        ResponseEntity<List<BillDTO>> responseEntity = new ResponseEntity<>(BillDTOS, HttpStatus.OK);
        return responseEntity;
    }
    @GetMapping("/searcher")
    public ResponseEntity<List<BillDTO>> findBySearchTerm(@RequestParam("searchTerm") String searchTerm){
        List<BillDTO> BillDTOS = billService.searchByTerm(searchTerm);
        ResponseEntity<List<BillDTO>> responseEntity = new ResponseEntity<>(BillDTOS, HttpStatus.OK);
        return responseEntity;
    }
}
