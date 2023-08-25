package ru.practicum.mainService.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.mainService.request.RequestStatus;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventRequestStatusUpdate {

    private RequestStatus status;
    private Set<Long> requestIds;

    public enum Status {

        CONFIRMED,
        REJECTED

    }

}