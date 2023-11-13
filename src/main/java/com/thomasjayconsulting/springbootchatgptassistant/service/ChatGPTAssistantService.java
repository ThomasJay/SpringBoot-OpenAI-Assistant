package com.thomasjayconsulting.springbootchatgptassistant.service;

import com.google.gson.Gson;
import com.thomasjayconsulting.springbootchatgptassistant.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Slf4j
public class ChatGPTAssistantService {


    @Value("${chatgpt.key}")
    private String CHATGPT_KEY;

    @Value("${chatgpt.model}")
    private String GPT_MODEL;

    @Value("${chatgpt.assistants}")
    private String CHATGPT_ASSISTANTS_URL;

    @Value("${chatgpt.threads}")
    private String CHATGPT_THREADS_URL;


    public Map<String, Object> createAssistant(String name, String instructions, List<Map<String, String>> tools) {

        log.info("createAssistant started. name: {} instructions: {} model: {} tools: {}", name, instructions, GPT_MODEL, tools);


        ChatGPTCreateAssistantRequest chatGPTCreateAssistantRequest = new ChatGPTCreateAssistantRequest();
        chatGPTCreateAssistantRequest.setModel(GPT_MODEL);
        chatGPTCreateAssistantRequest.setName(name);
        chatGPTCreateAssistantRequest.setInstructions(instructions);

        if (tools != null) {
            chatGPTCreateAssistantRequest.setTools(tools);
        }


        log.info("createAssistant started. url: {}", CHATGPT_ASSISTANTS_URL);

        HttpPost post = new HttpPost(CHATGPT_ASSISTANTS_URL);
        post.addHeader("Content-Type", "application/json");
        post.addHeader("Authorization", "Bearer " + CHATGPT_KEY);
        post.addHeader("OpenAI-Beta", "assistants=v1");

        Gson gson = new Gson();

        String body = gson.toJson(chatGPTCreateAssistantRequest);

        log.info("createAssistant post body: {}", body);

        try {
            final StringEntity entity = new StringEntity(body);
            post.setEntity(entity);

            try (CloseableHttpClient httpClient = HttpClients.custom().build();
                 CloseableHttpResponse response = httpClient.execute(post)) {

                String responseBody = EntityUtils.toString(response.getEntity());


                log.info("createAssistant got a status: {}", response.getStatusLine());
                log.info("createAssistant got a responseBody: {}", responseBody);

                ChatGPTCreateAssistantResponse chatGPTCreateAssistantResponse = gson.fromJson(responseBody, ChatGPTCreateAssistantResponse.class);

                log.info("createAssistant got a response: {}", chatGPTCreateAssistantResponse);
                log.info("createAssistant got id: {}", chatGPTCreateAssistantResponse.getId());

                Map<String, Object> map = new HashMap<>();

                map.put("status", "Success");
                map.put("assistantId", chatGPTCreateAssistantResponse.getId());

                return map;



            } catch (Exception e) {
                log.info("createAssistant exception A e: {}", e.getMessage());

                Map<String, Object> map = new HashMap<>();

                map.put("status", "Failed");
                map.put("content", e.getMessage());

                return map;
            }
        }
        catch (Exception e) {
            log.info("createAssistant exception B e: {}", e.getMessage());

            Map<String, Object> map = new HashMap<>();

            map.put("status", "Failed");
            map.put("content", e.getMessage());

            return map;
        }


    }

    public Map<String, Object> createThread() {

        log.info("createThread started.");

        log.info("createThread started. url: {}", CHATGPT_THREADS_URL);

        HttpPost post = new HttpPost(CHATGPT_THREADS_URL);
        post.addHeader("Content-Type", "application/json");
        post.addHeader("Authorization", "Bearer " + CHATGPT_KEY);
        post.addHeader("OpenAI-Beta", "assistants=v1");

        Gson gson = new Gson();

        String body = "";

        log.info("createThread post body: {}", body);

        try {
            final StringEntity entity = new StringEntity(body);
            post.setEntity(entity);

            try (CloseableHttpClient httpClient = HttpClients.custom().build();
                 CloseableHttpResponse response = httpClient.execute(post)) {

                String responseBody = EntityUtils.toString(response.getEntity());


                log.info("createThread got a status: {}", response.getStatusLine());
                log.info("createThread got a responseBody: {}", responseBody);

                ChatGPTCreateThreadResponse chatGPTCreateThreadResponse = gson.fromJson(responseBody, ChatGPTCreateThreadResponse.class);

                log.info("createThread got a response: {}", chatGPTCreateThreadResponse);
                log.info("createThread got id: {}", chatGPTCreateThreadResponse.getId());

                Map<String, Object> map = new HashMap<>();

                map.put("status", "Success");
                map.put("threadId", chatGPTCreateThreadResponse.getId());

                return map;



            } catch (Exception e) {
                log.info("createThread exception A e: {}", e.getMessage());

                Map<String, Object> map = new HashMap<>();

                map.put("status", "Failed");
                map.put("content", e.getMessage());

                return map;
            }
        }
        catch (Exception e) {
            log.info("createThread exception B e: {}", e.getMessage());

            Map<String, Object> map = new HashMap<>();

            map.put("status", "Failed");
            map.put("content", e.getMessage());

            return map;
        }


    }

    public Map<String, Object> sendMessageToThread(String message, String assistantId, String threadId) {


        log.info("sendMessageToThread started. message: {} assistantId: {} threadId: {}", message, assistantId, threadId);

        String url = CHATGPT_THREADS_URL + "/" + threadId + "/messages";

        log.info("sendMessageToThread started. url: {}", url);

        ChatGPTCreateMessageRequest chatGPTCreateMessageRequest = new ChatGPTCreateMessageRequest();

        // Note: Only user role is currently supported
        chatGPTCreateMessageRequest.setRole("user");
        chatGPTCreateMessageRequest.setContent(message);


        HttpPost post = new HttpPost(url);
        post.addHeader("Content-Type", "application/json");
        post.addHeader("Authorization", "Bearer " + CHATGPT_KEY);
        post.addHeader("OpenAI-Beta", "assistants=v1");

        Gson gson = new Gson();

        String body = gson.toJson(chatGPTCreateMessageRequest);

        log.info("sendMessageToThread post body: {}", body);

        try {
            final StringEntity entity = new StringEntity(body);
            post.setEntity(entity);

            try (CloseableHttpClient httpClient = HttpClients.custom().build();
                 CloseableHttpResponse response = httpClient.execute(post)) {

                String responseBody = EntityUtils.toString(response.getEntity());

                Map<String, Object> messageResponse = gson.fromJson(responseBody, Map.class);

                String messageId = (String)messageResponse.get("id");

                log.info("sendMessageToThread got a status: {}", response.getStatusLine());
                log.info("sendMessageToThread got a responseBody: {}", responseBody);

                String url2 = CHATGPT_THREADS_URL + "/" + threadId + "/runs";

                log.info("sendMessageToThread run. url: {}", url2);


                HttpPost runPost = new HttpPost(url2);
                runPost.addHeader("Content-Type", "application/json");
                runPost.addHeader("Authorization", "Bearer " + CHATGPT_KEY);
                runPost.addHeader("OpenAI-Beta", "assistants=v1");

                ChatGPTRunsRequest chatGPTRunsRequest = new ChatGPTRunsRequest();
                chatGPTRunsRequest.setAssistantId(assistantId);

                String body2 = gson.toJson(chatGPTRunsRequest);

                final StringEntity entity2 = new StringEntity(body2);
                runPost.setEntity(entity2);

                log.info("sendMessageToThread post run body2: {}", body2);


                // Retrieve run status for thread
                try (CloseableHttpClient httpClient2 = HttpClients.custom().build();
                     CloseableHttpResponse response2 = httpClient.execute(runPost)) {

                    String responseBody2 = EntityUtils.toString(response2.getEntity());


                    log.info("sendMessageToThread got a status2: {}", response2.getStatusLine());
                    log.info("sendMessageToThread got a responseBody2: {}", responseBody2);

                    Map<String, Object> messageResponse2 = gson.fromJson(responseBody2, Map.class);

                    String runId = (String)messageResponse2.get("id");

                    String url3 = CHATGPT_THREADS_URL + "/" + threadId + "/runs/" + runId;

                    log.info("sendMessageToThread Poll runs. url: {}", url3);

                    HttpGet runPollGet = new HttpGet(url2);
                    runPollGet.addHeader("Authorization", "Bearer " + CHATGPT_KEY);
                    runPollGet.addHeader("OpenAI-Beta", "assistants=v1");

                    boolean completed = false;
                    int loopCount = 0;

                    // Poll run status for thread using runId
                    do {
                        log.info("sendMessageToThread Start of polling..........");

                        loopCount++;

                        try (CloseableHttpClient httpClient3 = HttpClients.custom().build();
                             CloseableHttpResponse response3 = httpClient.execute(runPollGet)) {

                            String responseBody3 = EntityUtils.toString(response3.getEntity());

                            log.info("sendMessageToThread got a status3: {}", response3.getStatusLine());
                            log.info("sendMessageToThread got a responseBody3: {}", responseBody3);

                            Map<String, Object> runPollResponse = gson.fromJson(responseBody3, Map.class);

                            List<Object> dataItems = (List<Object>)runPollResponse.get("data");

                            if (dataItems != null && dataItems.size() > 0) {
                                Map<String, Object> item0 = (Map<String, Object>) dataItems.get(0);

                                String runStatus = (String) item0.get("status");

                                if (runStatus.equals("completed")) {
                                    completed = true;
                                    log.info("sendMessageToThread run completed..........Get Last Message");
                                    break;
                                }

                            }


                            if (loopCount > 15) {
                                break;
                            }

                            // Wait if not ready
                            try {
                                log.info("sendMessageToThread not ready, wait 1 second.....");

                                Thread.sleep(1000);
                            }
                            catch (InterruptedException e) {

                            }


                        }
                        catch(Exception e) {

                        }


                    }
                    while (true);

                    log.info("We can go get the messages line");

                    if (completed) {

                        Map<String, Object> lastMessage = getLastAssistantMessage(threadId);

                        Map<String, Object> map = new HashMap<>();

                        map.put("status", "Success");
                        map.put("message", lastMessage.get("value"));

                        return map;

                    }
                    else {
                        Map<String, Object> map = new HashMap<>();

                        map.put("status", "Failed to get Messages");

                        return map;

                    }



                } catch (Exception e) {
                    log.info("sendMessageToThread exception A e: {}", e.getMessage());

                    Map<String, Object> map = new HashMap<>();

                    map.put("status", "Failed");
                    map.put("content", e.getMessage());

                    return map;
                }


//                ChatGPTCreateThreadResponse chatGPTCreateThreadResponse = gson.fromJson(responseBody, ChatGPTCreateThreadResponse.class);
//
//                log.info("sendMessageToThread got a response: {}", chatGPTCreateThreadResponse);
//                log.info("sendMessageToThread got id: {}", chatGPTCreateThreadResponse.getId());
//
//                Map<String, Object> map = new HashMap<>();
//
//                map.put("status", "Success");
//                map.put("threadId", chatGPTCreateThreadResponse.getId());
//
//                return map;



            } catch (Exception e) {
                log.info("sendMessageToThread exception A e: {}", e.getMessage());

                Map<String, Object> map = new HashMap<>();

                map.put("status", "Failed");
                map.put("content", e.getMessage());

                return map;
            }
        }
        catch (Exception e) {
            log.info("sendMessageToThread exception B e: {}", e.getMessage());

            Map<String, Object> map = new HashMap<>();

            map.put("status", "Failed");
            map.put("content", e.getMessage());

            return map;
        }


    }

    public Map<String, Object> getLastAssistantMessage(String threadId) {

        log.info("getLastAssistantMessage started. threadId: {}", threadId);


        String url = CHATGPT_THREADS_URL + "/" + threadId + "/messages";

        log.info("getLastAssistantMessage started. url: {}", url);


        HttpGet get = new HttpGet(url);
        get.addHeader("Authorization", "Bearer " + CHATGPT_KEY);
        get.addHeader("OpenAI-Beta", "assistants=v1");

        Gson gson = new Gson();


        try {

            try (CloseableHttpClient httpClient = HttpClients.custom().build();
                 CloseableHttpResponse response = httpClient.execute(get)) {

                String responseBody = EntityUtils.toString(response.getEntity());


                log.info("getLastAssistantMessage got a status: {}", response.getStatusLine());
                log.info("getLastAssistantMessage got a responseBody: {}", responseBody);

                Map<String, Object> resp = gson.fromJson(responseBody, Map.class);

                List<Object> data = (List<Object>)resp.get("data");

                log.info("getLastAssistantMessage got data");

                Object firstMessageObject = data.get(0);

                log.info("getLastAssistantMessage firstMessageObject {}", firstMessageObject);

                Map<String, Object> firstMessage = (Map<String, Object>)firstMessageObject;

                List<Object> contents = (List<Object>)firstMessage.get("content");

                log.info("getLastAssistantMessage contents {}", contents);


                Map<String, Object> content = (Map<String, Object>)contents.get(0);

                Map<String, Object> textElement = (Map<String, Object>)content.get("text");


                log.info("getLastAssistantMessage textElement {}", textElement);

                String value = (String) textElement.get("value");

                Map<String, Object> map = new HashMap<>();

                map.put("status", "Success");
                map.put("value", value);

                log.info("getLastAssistantMessage value {}", value);

                return map;



            } catch (Exception e) {
                log.info("getLastAssistantMessage exception A e: {}", e.getMessage());

                e.printStackTrace();
                Map<String, Object> map = new HashMap<>();

                map.put("status", "Failed");
                map.put("content", e.getMessage());

                return map;
            }
        }
        catch (Exception e) {
            log.info("getLastAssistantMessage exception B e: {}", e.getMessage());

            Map<String, Object> map = new HashMap<>();

            map.put("status", "Failed");
            map.put("content", e.getMessage());

            return map;
        }


    }

    public Map<String, Object> getMessages(String threadId) {

        log.info("getMessages started. threadId: {}", threadId);


        String url = CHATGPT_THREADS_URL + "/" + threadId + "/messages";

        log.info("getMessages started. url: {}", url);


        HttpGet get = new HttpGet(url);
        get.addHeader("Authorization", "Bearer " + CHATGPT_KEY);
        get.addHeader("OpenAI-Beta", "assistants=v1");

        Gson gson = new Gson();


        try {

            try (CloseableHttpClient httpClient = HttpClients.custom().build();
                 CloseableHttpResponse response = httpClient.execute(get)) {

                String responseBody = EntityUtils.toString(response.getEntity());


                log.info("getMessages got a status: {}", response.getStatusLine());
                log.info("getMessages got a responseBody: {}", responseBody);

                Map<String, Object> resp = gson.fromJson(responseBody, Map.class);

                List<Object> messages = new ArrayList<>();

                // Get Data
                List<Object> dataList = (List<Object>)resp.get("data");

                for (Object obj : dataList) {

                    Map<String, Object> item = (Map<String, Object>)obj;

                    Map<String, Object> msg = new HashMap<>();

                    msg.put("created_at", item.get("created_at"));
                    msg.put("thread_id", item.get("thread_id"));
                    msg.put("role", item.get("role"));

                    List<Object> contents = (List<Object>)item.get("content");
                    Map<String, Object> content = (Map<String, Object>)contents.get(0);
                    Map<String, Object> textElement = (Map<String, Object>)content.get("text");
                    String value = (String) textElement.get("value");

                    msg.put("value", value);


                    messages.add(msg);
                }


                Map<String, Object> map = new HashMap<>();

                map.put("status", "Success");
                map.put("messages", messages);

                return map;



            } catch (Exception e) {
                log.info("getMessages exception A e: {}", e.getMessage());

                Map<String, Object> map = new HashMap<>();

                map.put("status", "Failed");
                map.put("content", e.getMessage());

                return map;
            }
        }
        catch (Exception e) {
            log.info("getMessages exception B e: {}", e.getMessage());

            Map<String, Object> map = new HashMap<>();

            map.put("status", "Failed");
            map.put("content", e.getMessage());

            return map;
        }


    }


}
