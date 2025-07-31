package com.example.gateway.conf;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class Carousel {
    private final EurekaClient eurekaClient;
    List<InstanceInfo> instances = new ArrayList<>();
    int currentIndex = 0;
    public Carousel(EurekaClient eurekaClient){
        this.eurekaClient = eurekaClient;
        initAuthCarousel();
        events();
    }
    public String getUriAuth(){
        StringBuilder stringBuilder = new StringBuilder();
        InstanceInfo instanceInfo = instances.get(currentIndex);
        stringBuilder.append(instanceInfo.getIPAddr()).append(":").append(instanceInfo.getPort());
        if(instances.size()-1==currentIndex){
            currentIndex=0;
        }
        else{
            currentIndex+=1;
        }
        return stringBuilder.toString();
    }
    private void events(){
        eurekaClient.registerEventListener(eurekaEvent ->{
            initAuthCarousel();
        });
        eurekaClient.unregisterEventListener(eurekaEvent -> {
            initAuthCarousel();
        });
    }
    public void initAuthCarousel(){
        instances = eurekaClient.getApplication("auth").getInstances();
    }
}
