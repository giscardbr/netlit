package br.com.netlit.oauth2.infra.messaging;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Notification {

  @JsonProperty("Type")
  private String type;
  @JsonProperty("MessageId")
  private String messageId;
  @JsonProperty("TopicArn")
  private String topicArn;
  @JsonProperty("Message")
  private String message;
  @JsonProperty("Timestamp")
  private String timestamp;
  @JsonProperty("SignatureVersion")
  private String signatureVersion;
  @JsonProperty("Signature")
  private String signature;
  @JsonProperty("SigningCertURL")
  private String signingCertURL;
  @JsonProperty("UnsubscribeURL")
  private String unsubscribeURL;
}
