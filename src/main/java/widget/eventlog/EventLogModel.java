package widget.eventlog;

import com.github.jasminb.jsonapi.annotations.Type;

import net.explorviz.extension.dashboard.main.BaseModel;
import net.explorviz.shared.landscape.model.event.EEventType;

@Type("eventlog")
public class EventLogModel extends BaseModel {

	private long timestampLandscape;
	private long timestampEvent;
	private EEventType eventType;
	private String eventMessage;

	public EventLogModel() {
		// default constructor for JSON API parsing
	}

	public EventLogModel(long timestampLandscape, long timestampEvent, EEventType eventType, String eventMessage) {
		super();
		this.timestampLandscape = timestampLandscape;
		this.timestampEvent = timestampEvent;
		this.eventType = eventType;
		this.eventMessage = eventMessage;
	}

	public long getTimestampLandscape() {
		return timestampLandscape;
	}

	public void setTimestampLandscape(long timestampLandscape) {
		this.timestampLandscape = timestampLandscape;
	}

	public long getTimestampEvent() {
		return timestampEvent;
	}

	public void setTimestampEvent(long timestampEvent) {
		this.timestampEvent = timestampEvent;
	}

	public EEventType getEventType() {
		return eventType;
	}

	public void setEventType(EEventType eventType) {
		this.eventType = eventType;
	}

	public String getEventMessage() {
		return eventMessage;
	}

	public void setEventMessage(String eventMessage) {
		this.eventMessage = eventMessage;
	}

	public String toString() {
		return "EventLogModel - timestampEvent: " + timestampEvent + " eventType: " + eventType.toString()
				+ " eventMessage: " + eventMessage;
	}

}
