package com.example.plantchatservice.repository.notification;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class EmitterRepository {
    //Map에 회원과 연결된 SSE SseEmitter 객체를 저장
    public final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    //Event를 캐시에 저장
    public final Map<String, Object> cache = new ConcurrentHashMap<>();
    //id, sseEmitter map에 저장
    public SseEmitter save(String id, SseEmitter sseEmitter) {
        emitters.put(id, sseEmitter);
        return sseEmitter;
    }
    //id와 event를 매개변수로 받아 cache 맵에 저장
    public void saveCache(String id, Object event) {
        cache.put(id, event);
    }
    //id로 시작하는 키를 가진 emitters 맵 항목을 필터링하여 맵으로 반환
    public Map<String, SseEmitter> findAllStartWithById(String id) {
        return emitters.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(id))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    //id로 시작하는 키를 가진 eventCache 맵 항목을 필터링하여 맵으로 반환
    public Map<String, Object> findAllCacheStartWithId(String id) {
        return cache.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(id))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
    // id로 시작하는 키를 가진 emitters 맵 항목을 모두 제거
    public void deleteAllStartWithId(String id) {
        emitters.forEach(
                (key, emitter) -> {
                    if (key.startsWith(id)) {
                        emitters.remove(key);
                    }
                }
        );
    }
    //  emitters 맵에서 해당 id를 가진 항목을 삭제
    public void deleteById(String id) {
        emitters.remove(id);
    }

    // id로 시작하는 키를 가진 eventCache 맵 항목을 모두 제거
    public void deleteAllCacheStartWithId(String id) {
        cache.forEach(
                (key, data) -> {
                    if (key.startsWith(id)) {
                        cache.remove(key);
                    }
                }
        );
    }
}
