package ru.practicum.item.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.practicum.util.Marker;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemDto {
    @NotBlank(groups = Marker.OnCreate.class)
    @Size(min = 1, max = 255,
            groups = {Marker.OnCreate.class,
                    Marker.OnUpdate.class})
    private String name;
    @NotBlank(groups = Marker.OnCreate.class)
    @Size(min = 1, max = 255,
            groups = {Marker.OnCreate.class,
                    Marker.OnUpdate.class})
    private String description;
    @NotNull(groups = Marker.OnCreate.class)
    private Boolean available;
    private Long requestId;
}