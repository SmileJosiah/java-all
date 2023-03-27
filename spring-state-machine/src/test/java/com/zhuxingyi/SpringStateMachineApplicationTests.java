package com.zhuxingyi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import reactor.core.publisher.Mono;

@SpringBootTest
class SpringStateMachineApplicationTests {


    @Autowired
    private StateMachine<States,Events> machine;

    @Test
    void contextLoads() {

    }

    @Test
    void stateMachineStartTest(){
        machine.sendEvent(Events.E1);
        machine.sendEvent(Events.E2);

    }

}
