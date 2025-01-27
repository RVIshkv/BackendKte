package com.tarasov.ktebackend.controllers.rest;

import com.tarasov.ktebackend.controllers.dto.GetClientsResponseDto;
import com.tarasov.ktebackend.controllers.dto.UpdateClientDiscountRequestDto;
import com.tarasov.ktebackend.controllers.dto.basic.BasicResponseDto;
import com.tarasov.ktebackend.entity.Client;
import com.tarasov.ktebackend.service.ClientService;
import com.tarasov.ktebackend.utils.mapper.DtoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.tarasov.ktebackend.utils.Validations.checkNotNegativeInteger;
import static com.tarasov.ktebackend.utils.Validations.checkNotNull;
import static com.tarasov.ktebackend.utils.Validations.checkUuid;

@RestController
@RequestMapping("/api2/client")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Клиенты", description = "Методы для работы с клиентами")
public class ClientController {
    private final ClientService clientService;
    private final DtoMapper dtoMapper;

    @Operation(summary = "Список клиентов")
    @GetMapping("/list")
    public GetClientsResponseDto getClients() {
        List<Client> clients = clientService.getClients();
        return GetClientsResponseDto.builder()
                .clients(
                        clients.stream()
                                .map(dtoMapper::clientToDto)
                                .collect(Collectors.toList())
                )
                .build();
    }

    @Operation(summary = "Изменение индивидуальных скидок клиента")
    @PostMapping("/discount/{uuid}")
    public BasicResponseDto updateClientDiscount(
            @Parameter(description = "ID клиента Ivan ", example = "7057279f-354c-45c1-983b-c4542613e5e8")
            @PathVariable("uuid") UUID clientUuid,
            @RequestBody UpdateClientDiscountRequestDto request
    ) {
        checkNotNull(clientUuid, "clientUuid");
        checkUuid(clientUuid, "clientUuid");
        checkNotNegativeInteger(request.getDiscountFirst(), "discountFirst");
        checkNotNegativeInteger(request.getDiscountSecond(), "discountSecond");

        clientService.changeDiscount(clientUuid, request.getDiscountFirst(), request.getDiscountSecond());

        return new BasicResponseDto();
    }


}
