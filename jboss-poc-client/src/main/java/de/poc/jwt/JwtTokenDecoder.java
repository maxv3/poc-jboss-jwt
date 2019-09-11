package de.poc.jwt;

import java.io.IOException;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JwtTokenDecoder {
  //**********************************************************************************
  // attributes
  //**********************************************************************************
  private static String mJwtTokenHeader = "";
  private static String mJwtTokenBody = "";
  private static String mJwtTokenSignature = "";

  //**********************************************************************************
  // lifecycle
  //**********************************************************************************

  //**********************************************************************************
  // implementation
  //**********************************************************************************
  public static JwtTokenDO getDecodedJwtToken(String pJwtToken) {
    JwtTokenDO lJwtTokenDO = null;
    try {
      lJwtTokenDO = JwtTokenDO.valueOf(decodeJwtToken(pJwtToken));
      lJwtTokenDO.setToken(pJwtToken);
    } catch (Exception lE) {
      System.err.println(">>> Error getDecodedJwtToken: " + lE.getMessage());
    }
    return lJwtTokenDO;
  }

  private static JwtTokenDTO decodeJwtToken(String pJwtToken) throws JsonParseException, JsonMappingException, IOException {
    final Decoder lBase64Decoder = Base64.getUrlDecoder();

    // split out the "parts" (header, payload and signature)
    final String[] lJwtTokenParts = pJwtToken.split("\\.");

    mJwtTokenHeader = lJwtTokenParts[0];
    mJwtTokenBody = new String(lBase64Decoder.decode(lJwtTokenParts[1]));
    mJwtTokenSignature = lJwtTokenParts[2];

    final ObjectMapper lObjMapper = new ObjectMapper();
    return lObjMapper.readValue(mJwtTokenBody, JwtTokenDTO.class);
  }

  public static String getEncodedJwtToken(JwtTokenDO pJwtToken) {
    final Encoder lBase64Encoder = Base64.getUrlEncoder();
    final ObjectMapper lObjMapper = new ObjectMapper();

    String lJwtTokenParts = "";
    try {
      lJwtTokenParts += mJwtTokenHeader + ".";
      lJwtTokenParts += lBase64Encoder.encodeToString(lObjMapper.writeValueAsBytes(JwtTokenDTO.valueOf(pJwtToken))) + ".";
      lJwtTokenParts += mJwtTokenSignature;
    } catch (Exception lE) {
      lE.printStackTrace();
    }

    return lJwtTokenParts;
  }
}
