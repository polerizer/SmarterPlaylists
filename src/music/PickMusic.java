/**
 * Nov 10, 2013
 * PickMusic.java
 * Daniel Pok
 * AP Java 6th
 */
package music;

import java.util.ArrayList;
import java.util.Collections;

import tools.SortByMetric;

import data.Coordinate;
import data.Track;
import data.Triplet;

/**
 * @author Daniel
 *
 */
public class PickMusic {

	public static ArrayList<Track> moodWithinDistance(ArrayList<Track> tracks, Track start, double dist){
		ArrayList<SortByMetric<Track>> results = new ArrayList<SortByMetric<Track>>();
		if(start.coordinate() == null) return new ArrayList<Track>();
		for(Track i: tracks){
			Coordinate sta = start.coordinate();
			Coordinate end = i.coordinate();
			if(end == null) continue;
			if(Math.abs(end.x - sta.x) > dist || Math.abs(end.y - sta.y) > dist){
				continue;
			} else{
				double distance = sta.distance(end);
				if(distance <= dist && !(i == start)){
					results.add(new SortByMetric<Track>(i, distance));
				}
			}
		}
		Collections.<SortByMetric<Track>>sort(results);	
		ArrayList<Track> list = new ArrayList<Track>();
		for(SortByMetric<Track> i:results){
			list.add(i.value());
		}

		return list;
	}

	public static ArrayList<Track> moodWithinDistance(ArrayList<Track> tracks, Coordinate start, double dist){
		ArrayList<SortByMetric<Track>> results = new ArrayList<SortByMetric<Track>>();
		if(start == null) return new ArrayList<Track>();
		for(Track i: tracks){
			Coordinate sta = start;
			Coordinate end = i.coordinate();
			if(end == null) continue;
			if(Math.abs(end.x - sta.x) > dist || Math.abs(end.y - sta.y) > dist){
				continue;
			} else{
				double distance = sta.distance(end);
				if(distance <= dist){
					results.add(new SortByMetric<Track>(i, distance));
				}
			}
		}
		Collections.<SortByMetric<Track>>sort(results);	
		ArrayList<Track> list = new ArrayList<Track>();
		for(SortByMetric<Track> i:results){
			list.add(i.value());
		}

		return list;
	}

	public static ArrayList<Track> bpmWithinDistance(ArrayList<Track> tracks, Track start, double dist){
		ArrayList<SortByMetric<Track>> results = new ArrayList<SortByMetric<Track>>();
		if(start.bpm() == null) return new ArrayList<Track>();
		for(Track i: tracks){
			Integer sta = start.bpm();
			Integer end = i.bpm();
			if(end == null) continue;
			double distance = Math.abs(sta - end);
			if(distance <= dist && !(i == start)){
				results.add(new SortByMetric<Track>(i, distance));
			}
		}
		Collections.<SortByMetric<Track>>sort(results);	
		ArrayList<Track> list = new ArrayList<Track>();
		for(SortByMetric<Track> i:results){
			list.add(i.value());
		}

		return list;
	}

	public static ArrayList<Track> bpmWithinDistance(ArrayList<Track> tracks, Integer start, double dist){
		ArrayList<SortByMetric<Track>> results = new ArrayList<SortByMetric<Track>>();
		if(start == null) return new ArrayList<Track>();
		for(Track i: tracks){
			Integer sta = start;
			Integer end = i.bpm();
			if(end == null) continue;
			double distance = Math.abs(sta - end);
			if(distance <= dist){
				results.add(new SortByMetric<Track>(i, distance));
			}
		}
		Collections.<SortByMetric<Track>>sort(results);	
		ArrayList<Track> list = new ArrayList<Track>();
		for(SortByMetric<Track> i:results){
			list.add(i.value());
		}

		return list;
	}
	
	public static ArrayList<Track> bpmInRange(ArrayList<Track> tracks, double min, double max){
		ArrayList<Track> results = new ArrayList<Track>();
		for(Track i: tracks){
			Integer end = i.bpm();
			if(end == null) continue;
			if(end >= min && end <= max){
				results.add(i);
			}
		}

		return results;
	}
	
	public static ArrayList<Track> sameGenre(ArrayList<Track> tracks, Track start){
		ArrayList<Track> one = new ArrayList<Track>(), two = new ArrayList<Track>(), three = new ArrayList<Track>(), combined = new ArrayList<Track>();
		ArrayList<Triplet> sta = start.genre(), end;
		if(sta == null || sta.size() <1){
			if(start.album().genre() == null || start.album().genre().size() < 1){
				return combined;	
			}else{
				sta = start.album().genre();
			}
		}
		for(Track i: tracks){
			if(i == start || i.genre() == null || i.genre().size() < 1){
				if(i.album().genre() == null || i.album().genre().size() < 1){
					continue;
				} else{
					end = i.album().genre();
				}
			} else{
				end = i.genre();				
			}
			if(sta.get(0) == end.get(0)){
				if(sta.size() > 1 && end.size() > 1 && sta.get(1) == end.get(1)){
					if(sta.size() > 2 && end.size() > 2 && sta.get(2) == end.get(2)){
						three.add(i);
					} else{
						two.add(i);
					}
				} else{
					one.add(i);
				}
			}
		}
		combined.addAll(three);
		combined.addAll(two);
		combined.addAll(one);

		return combined;
	}
	
	/**
	 * 
	 * @param tracks
	 * @param start
	 * @param depth a minimum of how many genre sets must match to be returned. I.e. depth = 3 means only perfect matches are returned while depth <= 1 means all matches are returned
	 * @return
	 */
	public static ArrayList<Track> sameGenre(ArrayList<Track> tracks, Track start, int depth){
		ArrayList<Track> one = new ArrayList<Track>(), two = new ArrayList<Track>(), three = new ArrayList<Track>(), combined = new ArrayList<Track>();
		ArrayList<Triplet> sta = start.genre(), end;
		if(sta == null || sta.size() <1){
			if(start.album().genre() == null || start.album().genre().size() < 1){
				return combined;	
			}else{
				sta = start.album().genre();
			}
		}
		for(Track i: tracks){
			if(i == start || i.genre() == null || i.genre().size() < 1){
				if(i.album().genre() == null || i.album().genre().size() < 1){
					continue;
				} else{
					end = i.album().genre();
				}
			} else{
				end = i.genre();				
			}

			if(sta.get(0) == end.get(0)){
				if(sta.size() > 1 && end.size() > 1 && sta.get(1) == end.get(1)){
					if(sta.size() > 2 && end.size() > 2 && sta.get(2) == end.get(2)){
						three.add(i);
					} else{
						two.add(i);
					}
				} else{
					one.add(i);
				}
			}
		}
		if(depth <= 3)combined.addAll(three);		
		if(depth <= 2) combined.addAll(two);
		if(depth <= 1) combined.addAll(one);
		return combined;
	}
	
}
