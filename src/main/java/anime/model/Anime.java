
package anime.model;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import anime.model.repository.MapAnimeRepository;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "title",
    "main_picture",
    "start_date",
    "end_date",
    "media_type",
    "status",
    "num_episodes",
    "start_season",
    "broadcast"
})
@Generated("jsonschema2pojo")
public class Anime {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("title")
    private String title;
    @JsonProperty("main_picture")
    private MainPicture mainPicture;
    @JsonProperty("start_date")
    private String startDate;
    @JsonProperty("end_date")
    private String endDate;
    @JsonProperty("media_type")
    private String mediaType;
    @JsonProperty("status")
    private String status;
    @JsonProperty("num_episodes")
    private Integer numEpisodes;
    @JsonProperty("start_season")
    private StartSeason startSeason;
    @JsonProperty("broadcast")
    private Broadcast broadcast;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("main_picture")
    public MainPicture getMainPicture() {
        return mainPicture;
    }

    @JsonProperty("main_picture")
    public void setMainPicture(MainPicture mainPicture) {
        this.mainPicture = mainPicture;
    }

    @JsonProperty("start_date")
    public String getStartDate() {
        return startDate;
    }

    @JsonProperty("start_date")
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    @JsonProperty("end_date")
    public String getEndDate() {
        return endDate;
    }

    @JsonProperty("end_date")
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @JsonProperty("media_type")
    public String getMediaType() {
        return mediaType;
    }

    @JsonProperty("media_type")
    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("num_episodes")
    public Integer getNumEpisodes() {
        return numEpisodes;
    }

    @JsonProperty("num_episodes")
    public void setNumEpisodes(Integer numEpisodes) {
        this.numEpisodes = numEpisodes;
    }

    @JsonProperty("start_season")
    public StartSeason getStartSeason() {
        return startSeason;
    }

    @JsonProperty("start_season")
    public void setStartSeason(StartSeason startSeason) {
        this.startSeason = startSeason;
    }

    @JsonProperty("broadcast")
    public Broadcast getBroadcast() {
        return broadcast;
    }

    @JsonProperty("broadcast")
    public void setBroadcast(Broadcast broadcast) {
        this.broadcast = broadcast;
    }

    public Double getAverageRating() {
		Double r =null;
		if(MapAnimeRepository.getInstance().getAverageRating(getId()) != 0)
			r = MapAnimeRepository.getInstance().getAverageRating(getId());
		return r;
	}
    

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
