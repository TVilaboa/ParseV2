package models;

import models.javacandidatestruct.CandidateClass;

import java.io.File;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Tomás on 12/01/2016.
 */
public class SaveWrapper implements Serializable{

    private List<CandidateClass> ccds;

    private List<File> files;

    public SaveWrapper(List<CandidateClass> ccds, List<File> files) {
        this.ccds = ccds;
        this.files = files;
    }

    public List<CandidateClass> getCcds() {
        return ccds;
    }

    public void setCcds(List<CandidateClass> ccds) {
        this.ccds = ccds;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }
}
