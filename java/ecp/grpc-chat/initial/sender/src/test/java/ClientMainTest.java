import org.junit.jupiter.api.*;

import io.grpc.inprocess.*;
import io.grpc.examples.chat.*;
import io.grpc.ManagedChannel;
import io.grpc.Server;
import reactor.core.publisher.Flux;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClientMainTest
{
    // display.readInput
    //write the output to a string
}