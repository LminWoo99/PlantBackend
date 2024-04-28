package Plant.PlantProject.service;


import Plant.PlantProject.Entity.Plant;
import Plant.PlantProject.dto.PlantDto;
import Plant.PlantProject.exception.ErrorCode;
import Plant.PlantProject.repository.PlantRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.RequiredArgsConstructor;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

@Service
@RequiredArgsConstructor
@Transactional
public class PlantService {
    private final PlantRepository plantRepository;

    private static final String API_KEY = "202212290SGQFNOOOQJTTHHYQIRA";
    private static final String LIST_URL = "http://api.nongsaro.go.kr/service/garden/gardenList?apiKey=" + API_KEY + "&pageNo=";
    private static final String DETAIL_URL = "http://api.nongsaro.go.kr/service/garden/gardenDtl?apiKey=" + API_KEY + "&cntntsNo=";
    private static final String IMAGE_URL ="http://api.nongsaro.go.kr/service/garden/gardenFileList?apiKey="+ API_KEY + "&cntntsNo=";



    public Page<PlantDto> plantList(String search, Pageable pageable) {
        Page<Plant> plants = plantRepository.findByPlantNameContaining(search, pageable);
        return plants.map(plant -> PlantDto.convertPlantToDto(plant));
    }

    public PlantDto plantDetail(Long id) {
        Plant plant = plantRepository.findById(id).orElseThrow(ErrorCode::throwPlantNotFound);
        return PlantDto.convertPlantToDto(plant);

    }
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
                byContentNum.setHumidity(hdCodeNm);
                byContentNum.setLight(lighttdemanddoCodeNm);
                byContentNum.setSpecial(speclmanageInfo);
                plantRepository.save(byContentNum);
            }



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getPlantNumbers(JSONArray itemArray, String key) {
        try {
            JSONArray array = new JSONArray(itemArray);
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsO = array.getJSONObject(i);
                String item = jsO.getString(key);
                getPlantDetail(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

////    public void start() throws IOException {
////        for (int i = 1; i < 23; i++) {
////            String pageURL = LIST_URL + i;
////            URL url = new URL(pageURL);
////            try (BufferedReader bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"))) {
////                StringBuilder rs = new StringBuilder();
////                String line;
////                while ((line = bf.readLine()) != null) {
////                    rs.append(line);
////                }
////                JSONObject jObject = XML.toJSONObject(rs.toString());
////                ObjectMapper mapper = new ObjectMapper();
////                mapper.enable(SerializationFeature.INDENT_OUTPUT);
////                Object json = mapper.readValue(jObject.toString(), Object.class);
////                String output = mapper.writeValueAsString(json);
////                JSONObject jsonObject = new JSONObject(output);
////                JSONObject response = jsonObject.getJSONObject("response");
////                JSONObject body = response.getJSONObject("body");
////                JSONObject items = body.getJSONObject("items");
////                JSONArray item = items.getJSONArray("item");
////
////                getPlantNumbers(item, "cntntsNo");
////            } catch (Exception e) {
////                e.printStackTrace();
////            }
////        }
//
//    }
    }
