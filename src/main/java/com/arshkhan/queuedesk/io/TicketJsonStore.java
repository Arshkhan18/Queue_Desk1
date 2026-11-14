package com.arshkhan.queuedesk.io;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.core.type.TypeReference;

import com.arshkhan.queuedesk.model.Ticket;
import com.arshkhan.queuedesk.exception.InvalidTicketDataException;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;


public class TicketJsonStore {

    //Creating Mapper to be used everywhere
    public static final ObjectMapper MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .enable(SerializationFeature.INDENT_OUTPUT);

    //Saving the java object to json
    public static void save(List<Ticket> tickets, Path file) throws InvalidTicketDataException {
        try{
            //creating parent directory if they don't exist
            Path parent = file.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            MAPPER.writerFor(new TypeReference<List<Ticket>>() {})
                    .writeValue(file.toFile(), tickets);
        }
        catch (IOException e){
            throw new InvalidTicketDataException( "Unable to save tickets to JSON file: " +
                    e.getMessage(), e);
        }
    }

    //Loading the json to java object
    public static List<Ticket> load(Path file) throws InvalidTicketDataException {
        try{
            List<Ticket>ticket= MAPPER.readValue(
                    file.toFile(),
                    new TypeReference<List<Ticket>>(){}
            );
            int highestId=ticket.stream()
                    .mapToInt(Ticket::getId)
                    .max()
                    .orElse(0);

          Ticket.updateNextId(highestId);

            return ticket;
        }catch (IOException e){
            throw new InvalidTicketDataException( "Unable to load tickets from JSON file: " +
                    e.getMessage(), e);
        }
    }
}
