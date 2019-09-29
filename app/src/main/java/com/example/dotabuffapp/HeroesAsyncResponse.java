package com.example.dotabuffapp;

public interface HeroesAsyncResponse {
    void heroesCountersProcessFinish(HeroesCounters heroesCounters);

    void heroesInitializationProcessFinish();
}
