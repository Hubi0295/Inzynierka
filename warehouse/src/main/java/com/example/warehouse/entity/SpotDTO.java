package com.example.warehouse.entity;


public class SpotDTO {
    private final long id;
    private final long shelf;
    private final long hall;
    private final long warehouse;

    private SpotDTO(Builder builder) {
        this.id = builder.id;
        this.shelf = builder.shelf;
        this.hall = builder.hall;
        this.warehouse = builder.warehouse;
    }

    public long getId() {
        return id;
    }

    public long getShelf() {
        return shelf;
    }

    public long getHall() {
        return hall;
    }

    public long getWarehouse() {
        return warehouse;
    }

    public static class Builder {
        private long id;
        private long shelf;
        private long hall;
        private long warehouse;

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder shelf(long shelf) {
            this.shelf = shelf;
            return this;
        }

        public Builder hall(long hall) {
            this.hall = hall;
            return this;
        }

        public Builder warehouse(long warehouse) {
            this.warehouse = warehouse;
            return this;
        }

        public SpotDTO build() {
            return new SpotDTO(this);
        }
    }
}