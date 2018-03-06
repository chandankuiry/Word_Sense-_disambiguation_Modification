
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class Classifier {	

	public static String score_count(List<String> target,Map<String,List<String>> context) {
		
		int count = 0;
		int semi_maxcount = 0;
		int maxcount = 0;
		String main_sense_target = null;
		String main_sense_context = null;
		String senses_res = null;
		String senses1_res = null;
		
		if(!context.entrySet().isEmpty()){
			
			
			//List<String> temp_target = new ArrayList<String>();
			Set<String> temp_target = new HashSet<String>();
			//List<String> temp_context = new ArrayList<String>();
			Set<String> temp_context = new HashSet<String>();
		
			for (Map.Entry<String, List<String>> entry : context.entrySet()){             // begin of loop 1
				
				
				for(String senses : target) {											  // begin of loop 2
					
					
					senses_res = Stopwords1.removeStopWords(senses);
					//System.out.println(senses);
					List<String> temp1 = new ArrayList<String>(Arrays.asList(senses_res.split(" ")));
					for(String stem_c : temp1){
						String x = Stemmertest.main1(stem_c);
						//System.out.println(x);
						if(!x.isEmpty())
						temp_target.add(x);
					}
					
					//temp_target = PorterCheck.completeStem(temp1);
					//**System.out.println(temp_target);
					
					
					for(String senses1 : entry.getValue()){									// begin of loop 3
						
						
						senses1_res = Stopwords1.removeStopWords(senses1);
						List<String> temp2 = new ArrayList<String>(Arrays.asList(senses1_res.split(" ")));
						//System.out.println("checking stemming"+temp2);
						//System.out.println("hello");
						for(String stem_c : temp2){
							String x = Stemmertest.main1(stem_c);
							if(!x.isEmpty())
							temp_context.add(x);
						}
						//**System.out.print(entry.getKey() +" -> "+ temp_context);
						//temp_context = PorterCheck.completeStem(temp2);
					
						count = 0;
						for(String comp_t : temp_target){                                  // begin of loop 4
							for(String comp_c : temp_context ){                            // begin of loop 5
								
								if(comp_c.contains(comp_t) || comp_c.contentEquals(comp_t) || comp_t.contains(comp_c) || comp_t.contentEquals(comp_c)){                               // begin of loop 6
									count++;
								}                                                          // end of loop 6	
							}                                                              // end of loop 5
						}																   // end of loop 4
						
						if(count > semi_maxcount){                                         // begin of loop 7
							semi_maxcount = count;
							if(semi_maxcount > maxcount){                                  // begin of loop 8
								maxcount = semi_maxcount;
								main_sense_target = senses;
								main_sense_context = senses1;
							}	// send the value of semi_maxcount from here to some 2d matrix.   //end of loop 8
						}                                                                         // end of loop 7
						//**System.out.println(" count ->"+count);
						temp_context.clear();
					}                                                                             // end of loop 3
					semi_maxcount = 0; 	
					temp_target.clear();
					//System.out.println();
					//System.out.println();
		        }                                                                                 // end of loop 2 
				//System.out.println(main_sense_target);
				//System.out.println(main_sense_context);	
			}                                                                                     // end of loop 1
			
			if(maxcount == 0){
				for(String senses: target){
					main_sense_target = senses;
					break;
				}
			}
			return main_sense_target;
		}
		else{												/// IF the context word set is empty
			
			for(String senses: target){
				main_sense_target = senses;
				break;
			}
				
		}
			
			
		return main_sense_target;
		
	}

}

















//context.forEach((a,b)->System.out.println(a,b));
//	String[] sentence = {"play","cricket","field"};

//List<String> target = new ArrayList<String>();






/*
// to read from a target file, here "play"

final String filename = "src/Files/target.txt";
BufferedReader br = null;
FileReader fr = null;

try {

	fr = new FileReader(filename);
	br = new BufferedReader(fr);

	String sCurrentLine;

	br = new BufferedReader(new FileReader(filename));

	while ((sCurrentLine = br.readLine()) != null) {
		//System.out.println(sCurrentLine);
		target.add(sCurrentLine);
		
	}

} catch (IOException e) {

	e.printStackTrace();

}


// to read from context words, here "field"

final String filename2 = "src/Files/context2.txt";
BufferedReader br2 = null;
FileReader fr2 = null;

try {

	fr2 = new FileReader(filename2);
	br2 = new BufferedReader(fr2);

	String sCurrentLine;

	br2 = new BufferedReader(new FileReader(filename2));

	while ((sCurrentLine = br2.readLine()) != null) {
		//System.out.println(sCurrentLine);
		context2.add(sCurrentLine);
		
	}

} catch (IOException e) {

	e.printStackTrace();

}


*/