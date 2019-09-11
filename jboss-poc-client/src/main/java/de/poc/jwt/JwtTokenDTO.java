package de.poc.jwt;

import java.time.ZoneId;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonAutoDetect(
    fieldVisibility = Visibility.ANY, 
    getterVisibility = Visibility.NONE, 
    setterVisibility = Visibility.NONE, 
    creatorVisibility = Visibility.NONE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"token_type", "iss", "aud", "sub", "jti", "iat", "exp", "nbf"})
public class JwtTokenDTO {
  //******************************************************************
  // attributes
  //******************************************************************
  @JsonProperty("token_type")
  private String mTokenType;

  @JsonProperty("iss")
  private String mIssuer;

  @JsonProperty("aud")
  private List<String> mAudience;

  @JsonProperty("sub")
  private String mSub;

  @JsonProperty("jti")
  private String mJti;

  @JsonProperty("iat")
  private Long mIat;

  @JsonProperty("exp")
  private Long mExp;

  @JsonProperty("nbf")
  private Long mNbf;

  //******************************************************************
  // lifecycle
  //****************************************************************

  //******************************************************************
  // implementation
  //******************************************************************
  public String getTokenType() {
    return mTokenType;
  }

  public void setTokenType(String pTokenType) {
    mTokenType = pTokenType;
  }

  public String getIss() {
    return mIssuer;
  }

  public void setIss(String pIssuer) {
    mIssuer = pIssuer;
  }

  public List<String> getAud() {
    return mAudience;
  }

  public void setAud(List<String> pAudience) {
    mAudience = pAudience;
  }

  public String getSub() {
    return mSub;
  }

  public void setSub(String pSub) {
    mSub = pSub;
  }

  public String getJti() {
    return mJti;
  }

  public void setJti(String pJti) {
    mJti = pJti;
  }

  public Long getIat() {
    return mIat;
  }

  public void setIat(Long pIat) {
    mIat = pIat;
  }

  public Long getExp() {
    return mExp;
  }

  public void setExp(Long pExp) {
    mExp = pExp;
  }

  public Long getNbf() {
    return mNbf;
  }

  public void setNbf(Long pNbf) {
    mNbf = pNbf;
  }

  public static JwtTokenDTO valueOf(JwtTokenDO pJwtTokenDO) {
    final JwtTokenDTO lJwtTokenDTO = new JwtTokenDTO();

    lJwtTokenDTO.setTokenType(pJwtTokenDO.getTokenType());
    lJwtTokenDTO.setIss(pJwtTokenDO.getIssuer());
    lJwtTokenDTO.setAud(pJwtTokenDO.getAudience());
    lJwtTokenDTO.setSub(pJwtTokenDO.getSub());
    lJwtTokenDTO.setJti(pJwtTokenDO.getJti());

    final ZoneId zoneId = ZoneId.systemDefault();

    lJwtTokenDTO.setIat(pJwtTokenDO.getIat().atZone(zoneId).toEpochSecond());
    lJwtTokenDTO.setExp(pJwtTokenDO.getExp().atZone(zoneId).toEpochSecond());
    lJwtTokenDTO.setNbf(pJwtTokenDO.getNbf().atZone(zoneId).toEpochSecond());

    return lJwtTokenDTO;
  }
}
