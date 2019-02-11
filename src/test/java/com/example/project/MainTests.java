package com.example.project;

import org.junit.jupiter.api.Test;

import java.util.List;

import com.example.project.base.Config;
import com.example.project.base.Game;
import com.example.project.base.Main;
import com.example.project.base.State;
import com.example.project.rules.Rule;
import com.example.project.rules.StoreRule;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

public class MainTests {
    @Test
    void testCleanUp() {
        List<State> list = new ArrayList<State>();
        list.add(new State(new Game(0, 0, 0, new Rule[] {
            Rule.makeRule(Config.STORE)
        })));
        list.add(new State(list.get(0), Rule.makeRule(Config.STORE), false));
        list.add(new State(list.get(1), new StoreRule(0), false)); // deleted
        list.add(new State(list.get(2), Rule.makeRule(Config.STORE), true));
        list.add(new State(list.get(3), Rule.makeRule(Config.STORE), true));
        list.add(new State(list.get(4), new StoreRule(0), false)); // deleted

        Main.cleanUp(list);

        System.out.println(list);
        assertEquals(4, list.size());
    }
}
