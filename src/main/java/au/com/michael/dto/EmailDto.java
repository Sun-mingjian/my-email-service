package au.com.michael.dto;

import au.com.michael.validator.EmailAddressDuplicationValidation;
import au.com.michael.validator.RecipientsValidation;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@JsonDeserialize
@EmailAddressDuplicationValidation(message = "EmailAddressDuplicationValidation.duplicate")
public class EmailDto {

    @NotNull(message = "NotNull.fromSender")
    @JsonProperty("from")
    private String fromSender;

    @Builder.Default
    @RecipientsValidation(message = "RecipientsValidation.value")
    @JsonProperty("to")
    private List<String> recipients = new ArrayList<>();

    @Builder.Default
    @JsonProperty("cc")
    private List<String> ccRecipients = new ArrayList<>();

    @Builder.Default
    @JsonProperty("bcc")
    private List<String> bccRecipients = new ArrayList<>();

    @NotNull(message = "NotNull.content")
    private String content;

    @NotNull(message = "NotNull.subject")
    private String subject;
}
