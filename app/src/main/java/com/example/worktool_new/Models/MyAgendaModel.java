package com.example.worktool_new.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class MyAgendaModel {
    @SerializedName("data")
    @Expose
    private ArrayList<Datum> data = null;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status_code")
    @Expose
    private Integer statusCode;

    public ArrayList<Datum> getData() {
        return this.data;
    }

    public void setData(ArrayList<Datum> data2) {
        this.data = data2;
    }

    public Integer getStatusCode() {
        return this.statusCode;
    }

    public void setStatusCode(Integer statusCode2) {
        this.statusCode = statusCode2;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message2) {
        this.message = message2;
    }

    public class Datum {
        @SerializedName("date")
        @Expose
        private String date;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("event_type")
        @Expose
        private String eventType;
        @SerializedName("idCompteAuteur")
        @Expose
        private String idCompteAuteur;
        @SerializedName("idEvent")
        @Expose
        private String idEvent;
        @SerializedName("idEventphoto")
        @Expose
        private String idEventphoto;
        @SerializedName("idFichier")
        @Expose
        private String idFichier;
        @SerializedName("resume")
        @Expose
        private String resume;
        @SerializedName("rubrique")
        @Expose
        private String rubrique;
        @SerializedName("statut")
        @Expose
        private String statut;
        @SerializedName("titre")
        @Expose
        private String titre;

        public Datum() {
        }

        public String getIdEvent() {
            return this.idEvent;
        }

        public void setIdEvent(String idEvent2) {
            this.idEvent = idEvent2;
        }

        public String getIdCompteAuteur() {
            return this.idCompteAuteur;
        }

        public void setIdCompteAuteur(String idCompteAuteur2) {
            this.idCompteAuteur = idCompteAuteur2;
        }

        public String getTitre() {
            return this.titre;
        }

        public void setTitre(String titre2) {
            this.titre = titre2;
        }

        public String getRubrique() {
            return this.rubrique;
        }

        public void setRubrique(String rubrique2) {
            this.rubrique = rubrique2;
        }

        public String getResume() {
            return this.resume;
        }

        public void setResume(String resume2) {
            this.resume = resume2;
        }

        public String getDescription() {
            return this.description;
        }

        public void setDescription(String description2) {
            this.description = description2;
        }

        public String getIdFichier() {
            return this.idFichier;
        }

        public void setIdFichier(String idFichier2) {
            this.idFichier = idFichier2;
        }

        public String getIdEventphoto() {
            return this.idEventphoto;
        }

        public void setIdEventphoto(String idEventphoto2) {
            this.idEventphoto = idEventphoto2;
        }

        public String getStatut() {
            return this.statut;
        }

        public void setStatut(String statut2) {
            this.statut = statut2;
        }

        public String getDate() {
            return this.date;
        }

        public void setDate(String date2) {
            this.date = date2;
        }

        public String getEventType() {
            return this.eventType;
        }

        public void setEventType(String eventType2) {
            this.eventType = eventType2;
        }
    }
}
