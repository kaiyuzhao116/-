package com.example.agent;

import java.util.concurrent.atomic.AtomicBoolean;

public sealed interface Phase {
    record Idle() implements Phase {}
    record Running(AtomicBoolean abort) implements Phase {}
    record Maintenance() implements Phase {}
}
