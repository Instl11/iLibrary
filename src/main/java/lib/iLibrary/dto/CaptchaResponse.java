package lib.iLibrary.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter @Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CaptchaResponse {

    private boolean success;
    @JsonProperty("error-codes")
    private Set<String> errors;
}
