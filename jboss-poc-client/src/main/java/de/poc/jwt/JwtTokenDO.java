package de.poc.jwt;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class JwtTokenDO {
  //******************************************************************
  // attributes
  //******************************************************************
  private String mTokenType;
  private String mIssuer;
  private List<String> mAudience;
  private String mSub;
  private String mJti;
  private LocalDateTime mIat;
  private LocalDateTime mExp;
  private LocalDateTime mNbf;
  private String mToken;

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

  public String getIssuer() {
    return mIssuer;
  }

  public void setIssuer(String pIssuer) {
    mIssuer = pIssuer;
  }

  public List<String> getAudience() {
    return mAudience;
  }

  public void setAudience(List<String> pAudience) {
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

  public LocalDateTime getIat() {
    return mIat;
  }

  public void setIat(LocalDateTime pIat) {
    mIat = pIat;
  }

  public LocalDateTime getExp() {
    return mExp;
  }

  public void setExp(LocalDateTime pExp) {
    mExp = pExp;
  }

  public LocalDateTime getNbf() {
    return mNbf;
  }

  public void setNbf(LocalDateTime pNbf) {
    mNbf = pNbf;
  }

  public String getToken() {
    return mToken;
  }

  public void setToken(String pToken) {
    mToken = pToken;
  }

  public static JwtTokenDO valueOf(JwtTokenDTO pDecodeJwtToken) {
    final JwtTokenDO lJwtTokenDO = new JwtTokenDO();
    lJwtTokenDO.setTokenType(pDecodeJwtToken.getTokenType());
    lJwtTokenDO.setIssuer(pDecodeJwtToken.getIss());
    lJwtTokenDO.setAudience(pDecodeJwtToken.getAud());
    lJwtTokenDO.setSub(pDecodeJwtToken.getSub());
    lJwtTokenDO.setJti(pDecodeJwtToken.getJti());
    lJwtTokenDO.setIat(parseDate(pDecodeJwtToken.getIat()));
    lJwtTokenDO.setExp(parseDate(pDecodeJwtToken.getExp()));
    lJwtTokenDO.setNbf(parseDate(pDecodeJwtToken.getNbf()));

    return lJwtTokenDO;
  }

  @Override
  public String toString() {
    String lJwtToken = ">>> JwtTokenDO";
    lJwtToken += "\n>>> -------------------------";
    lJwtToken += "\n>>> TokenType: " + mTokenType;
    lJwtToken += "\n>>> Issuer: " + mIssuer;
    lJwtToken += "\n>>> Audience: " + mAudience;
    lJwtToken += "\n>>> Sub: " + mSub;
    lJwtToken += "\n>>> JwtID: " + mJti;
    lJwtToken += "\n>>> IssuedAt: " + mIat;
    lJwtToken += "\n>>> ExpiresAt: " + mExp;
    lJwtToken += "\n>>> NotValidBefore: " + mNbf;
    lJwtToken += "\n>>> Token: " + mToken;

    return lJwtToken;
  }

  private static LocalDateTime parseDate(Long pDate) {
    return LocalDateTime.ofInstant(Date.from(Instant.ofEpochMilli(pDate * 1000)).toInstant(), ZoneId.systemDefault());
  }
}
