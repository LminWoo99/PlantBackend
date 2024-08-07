package Plant.PlantProject.service.plantinfo;


import Plant.PlantProject.domain.Entity.Plant;
import Plant.PlantProject.repository.plantinfo.PlantRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.RequiredArgsConstructor;

import org.json.JSONObject;
import org.json.XML;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

@Service
@RequiredArgsConstructor
@Transactional
public class PlantApi {
    private final PlantRepository plantRepository;

    private static final String API_KEY = "202212290SGQFNOOOQJTTHHYQIRA";
    private static final String DETAIL_URL = "http://api.nongsaro.go.kr/service/garden/gardenDtl?apiKey=" + API_KEY + "&cntntsNo=";


    public void getPlantDetail(String item) throws IOException {
        String pageURL = DETAIL_URL + item;
        URL url = new URL(pageURL);
        try (BufferedReader bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"))) {
            StringBuilder rs = new StringBuilder();
            String line;
            while ((line = bf.readLine()) != null) {
                rs.append(line);
            }
            JSONObject jObject = XML.toJSONObject(rs.toString());
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            Object json = mapper.readValue(jObject.toString(), Object.class);
            String output = mapper.writeValueAsString(json);
            JSONObject jsonObject = new JSONObject(output);
            JSONObject response = jsonObject.getJSONObject("response");
            JSONObject body = response.getJSONObject("body");
            JSONObject items = body.getJSONObject("item");
            //식물학명
            String cntntsNo = items.optString("cntntsNo", "null");
            String postngplaceCodeNm = items.optString("postngplaceCodeNm", "null");
            String hdCodeNm = items.optString("hdCodeNm", "null");
            String lighttdemanddoCodeNm = items.optString("lighttdemanddoCodeNm", "null");
            String speclmanageInfo = items.optString("speclmanageInfo", "null");
            Plant byContentNum = plantRepository.findByContentNum(cntntsNo);
            System.out.println("byContentNum = " + byContentNum);
            if(byContentNum !=null){
                byContentNum.addInfo(hdCodeNm, lighttdemanddoCodeNm, speclmanageInfo, speclmanageInfo);

                plantRepository.save(byContentNum);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    }


