import java.io.IOException;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import edu.smu.tspell.wordnet.AdjectiveSynset;
import edu.smu.tspell.wordnet.AdverbSynset;
import edu.smu.tspell.wordnet.NounSynset;
import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.VerbSynset;
import edu.smu.tspell.wordnet.WordNetDatabase;
import edu.smu.tspell.wordnet.WordSense;
import edu.smu.tspell.wordnet.impl.file.Morphology;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;


public class LeskWSD {
	
	List<String> NN;//,VB[],ADJ[],ADV[];
	List<String> VB;
	List<String> ADJ;
	List<String> ADV;
	String final_sense;
	String context_bag="",stem="";
	List<String> context_bag_stem;
	Map<String, String > contextword;
	public LeskWSD(){
		NN=new ArrayList<String>();
		VB=new ArrayList<String>();
		ADJ=new ArrayList<String>();
		ADV=new ArrayList<String>();
		contextword=new HashMap<String, String>();  //use to stored context word with its proper POS type if it isn't a stop word
	}
	public String preprocessing(String str,String target) throws IOException, ClassNotFoundException{
		//take input
		//senetnce and tagging it using
		//stanford POS Tagger.
		
		//**System.out.println("I/P Sentence := "+str+",	Target word := "+target);
		
		//creating a tagger object to tag the input 
		//senetnce wtih proper POS
		MaxentTagger tagger=new MaxentTagger("Taggers/english-left3words-distsim.tagger");
		String st=tagger.tagString(str); //here, input senetnce 'str' is tagged with appropriate POS
		//**System.out.println("======== I/P senetnce after Tagging ==== : "+st );
		String str1[]=st.split(" ");
		/*System.out.println();
		for(int i=0;i<str1.length;i++){
			System.out.println(str1[i]);
		}*/
		//int counter=0;
		for(int i=0;i<str1.length;i++){
			char ch[]=str1[i].toCharArray();
			int index=str1[i].indexOf('_'); 
			//System.out.println("checking "+index);
			//System.out.println("before checking stop word " +str1[i].substring(0, index));
			if(Stopwords1.isStopword(str1[i].substring(0, index))){
				//System.out.println("after checking stop word is : "+str1[i].substring(0, index));
				continue;
			}
				if(ch[index+1]=='N'){
					
					stem=Stemmertest.main1(str1[i].substring(0, index));
					
					//NN.add(Stemmertest.main1(str1[i].substring(0, index)));
					NN.add(stem);
					if(!stem.equalsIgnoreCase(target)){
						contextword.put(stem,"NOUN");
					}
					context_bag=context_bag+" "+ str1[i].substring(0, index); // creatting context bag after removing stop words.
				}
				if(ch[index+1]=='V'){
					stem="";
					/*if(str1[i].substring(0, index).equals("am") || str1[i].substring(0, index).equals("are") || str1[i].substring(0, index).equals("is") || str1[i].substring(0, index).equals("was") || str1[i].substring(0, index).equals("were") || str1[i].substring(0, index).equals("has") || str1[i].substring(0, index).equals("have") || str1[i].substring(0, index).equals("will") || str1[i].substring(0, index).equals("shall") || str1[i].substring(0, index).equals("should") || str1[i].substring(0, index).equals("would") || str1[i].substring(0, index).equals("may") || str1[i].substring(0, index).equals("can") || str1[i].substring(0, index).equals("might") || str1[i].substring(0, index).equals("could")){
						continue;
					}*/
					stem=Stemmertest.main1(str1[i].substring(0, index));
					VB.add(stem);
					if(!stem.equalsIgnoreCase(target)){
						contextword.put(stem,"VERB");
					}
					context_bag=context_bag + " " + str1[i].substring(0, index);
				}
				if(ch[index+1]=='J'){
					stem="";
					stem=Stemmertest.main1(str1[i].substring(0, index));
					ADJ.add(stem);
					if(!stem.equalsIgnoreCase(target)){
						contextword.put(stem,"ADJECTIVE");
					}
					context_bag=context_bag + " " + str1[i].substring(0, index);
				}
				if(ch[index+1]=='R'){
					stem="";
					stem=Stemmertest.main1(str1[i].substring(0, index));
					ADV.add(stem);
					if(!stem.equalsIgnoreCase(target)){
						contextword.put(stem,"ADVERB");
					}
					context_bag=context_bag + " " + str1[i].substring(0, index);
				}
			
		}//end of outer loop
		context_bag.replaceFirst(" ", ""); //to remove space since string context_bag is initially empty.
		//**System.err.println("Context bag including target word  after removing stop words : "+context_bag);
		/*
		System.out.print("Context word in Map :  ");
		for(String ctx : contextword.keySet()){
			System.out.print(ctx+"\t");
		}
		*/
		
		System.out.println();
		context_bag_stem=new ArrayList<String>();
		//convert the context bag into string array
		// so that each individual word can be passed to the
		//stemmer class to get proper stem
		String [] context_str=context_bag.split(" ");
		for(int i2=0;i2<context_str.length;i2++){
		context_bag_stem.add(Stemmertest.main1(context_str[i2]));
		}
		//**System.err.print("Total no. of context words in stemmed form  :  ");
		context_bag_stem.remove(0);  //It will remove the first element of list which a space character
		context_bag_stem.remove(target);
		/* **for (String string : context_bag_stem) {
			System.out.println(string+"\t ");
		}*/
		//context_bag_stem.clear();
		
		//**System.out.println("\n----------------------------------------------------------");
		//System.out.println("context bag lenght : "+context_bag.length());
		//print the total number of contents word(NN, VB, JJ, RB)
		/* **
		for (String st1 : NN) {
			counter++;
			if(counter==1){
				System.out.print("Total Nouns : ");
			}
			System.out.print(st1+"	");
		}
		System.out.println();
		//if(counter==0)System.out.println("No Nouns are available in the given context..\n");
		counter=0;
		for (String st1 : VB) {
			counter++;
			if(counter==1){
				System.out.print("Total Verbs : ");
			}
			System.out.print(st1+"	");
		}
		System.out.println();
		//if(counter==0)System.out.print("No Verbs are available in the given context..\n");
		counter=0;
		for (String st1 : ADJ) {
			counter++;
			if(counter==1){
				System.out.print("Total Adjectives : ");
			}
			System.out.print(st1+"	");
		}
		System.out.println();
		//if(counter==0)System.out.println("No Adjectives are available in the given context..\n");
		counter=0;
		for (String st1 : ADV) {
			counter++;
			if(counter==1){
				System.out.print("Total Adverbs : ");
			}
			System.out.print(st1+"	");
		}
		
		*/
		//if(counter==0)System.out.println("No Adverbs are available in the given context..");
		//**System.out.println("\n------------------------------------------------------------");
		//Identifying the senses of the word(Noun)
		
		//System.out.println("\n.... Identifying the synsets ....");
		System.setProperty("wordnet.database.dir", "C:/WordNet-3.0/dict/");
		NounSynset nounsynset;
		//NounSynset[] hyponyms;
		Synset[] synset;
		//WordSense wordsense[];
		List<String> senses =new ArrayList<String>();
		List<String> senses1;
		Map<String,List<String>> context_word_sense=new HashMap<String, List<String>>();
		int i;
		int	count=0;
		WordNetDatabase wdatabase=WordNetDatabase.getFileInstance();
		
		Morphology id = Morphology.getInstance();
		/*
		String base[]=id.getBaseFormCandidates("football", SynsetType.NOUN);
		System.out.println("---- base form of words ----");
		for(int k=0;k<base.length;k++){
			System.out.println(base[0]);
		}
		System.out.println("---- endof base[] ----");
		*/
	    for(String st1 : NN){
			//System.out.println("string in noun array NN : "+st1);
			if(st1!=null){
				//String base[]=id.getBaseFormCandidates(st1, SynsetType.NOUN);
				if(st1.equalsIgnoreCase(target) && count==0){
					//System.out.println("if st1==target in NN");
					//**System.out.println(".. synset of nouns.. ");
			      synset=wdatabase.getSynsets(st1,SynsetType.NOUN);
			      count++;
			     //** System.out.println("Total Number of senses : "+synset.length);
			      if(synset.length==1){
			    	  System.out.println("This word has single sense as a noun");
			    	  final_sense=((NounSynset)synset[0]).getDefinition();
			      }
			      else{
			    	
			      //Retrieving the senses of the target word
			     
			   //   System.out.print("WordForms"+"\t"+"TagCount"+"\t "+"   Sense Definition\n");
				 // System.out.println("---------"+" ***** "+"--------"+" ****** "+" ---------------------");
			      for (Synset synset2 : synset) {
				    nounsynset=(NounSynset) synset2;
				    //to get actual word and its senses
				   // System.out.print(nounsynset.getWordForms()[0] + "\t\t   " +nounsynset.getTagCount(nounsynset.getWordForms()[0]) +"\t\t   "+ nounsynset.getDefinition()+"\n");
				    senses.add(nounsynset.getDefinition());
			      }
			      //System.out.println("--------"+"********"+"---------"+"********"+"------------------------");
			      
			      // Retrieving the senses of the context words and stored in a Map.
			      for(Map.Entry<String, String> ctx : contextword.entrySet()){    //outer loop starts
			    	  String key=ctx.getKey().toString();
			    	  String value=ctx.getValue().toString();
			    	 // senses1.clear();
			    	  if(value.equalsIgnoreCase("NOUN")){
			    		//  System.out.println("---- if context word is noun ----");
			    		  synset=wdatabase.getSynsets(key,SynsetType.NOUN);
			    		  //System.out.print("WordForms"+"\t"+"TagCount"+"\t "+"   Sense Definition\n");
						  //System.out.println("---------"+" ***** "+"--------"+" ****** "+" ---------------------");
						  senses1=new ArrayList<String>();
					      for (Synset synset2 : synset) {
						    nounsynset=(NounSynset) synset2;
						    //to get actual word and its senses
						  //  System.out.println("1111111111111111111111");
						    //System.out.print(nounsynset.getWordForms()[0] + "\t\t   " +nounsynset.getTagCount(nounsynset.getWordForms()[0]) +"\t\t   "+ nounsynset.getDefinition()+"\n");
						    senses1.add(nounsynset.getDefinition());
						    //**System.out.println("checking definition for noun"+nounsynset.getDefinition());
					      }
					      //System.out.println("--------"+"********"+"---------"+"********"+"------------------------");
					      context_word_sense.put(key, senses1);  //context word senses are stored in map.
					      
			    	  }
			    	  else if(value.equalsIgnoreCase("VERB")){
			    		  //System.out.println("---- if context word is verb ----");
			    		  synset=wdatabase.getSynsets(key,SynsetType.VERB);
			    		  //System.out.print("WordForms"+"\t"+"TagCount"+"\t "+"   Sense Definition\n");
						  //System.out.println("---------"+" ***** "+"--------"+" ****** "+" ---------------------");
						  senses1=new ArrayList<String>();
					      for (Synset synset2 : synset) {
						    VerbSynset verbsynset=(VerbSynset) synset2;
						    //to get actual word and its senses
						    //System.out.print(verbsynset.getWordForms()[0] + "\t\t   " +verbsynset.getTagCount(verbsynset.getWordForms()[0]) +"\t\t   "+ verbsynset.getDefinition()+"\n");
						    senses1.add(verbsynset.getDefinition());
						    //**System.out.println("checking for definition"+verbsynset.getDefinition());
					      }
					      //System.out.println("--------"+"********"+"---------"+"********"+"------------------------");
					      context_word_sense.put(key, senses1);  //context word senses are stored in map.
					      //senses1.clear();
			    	  }
			    	  else if(value.equalsIgnoreCase("ADVERB")){
			    		  //System.out.println("---- if context word is adverb ----");
			    		  synset=wdatabase.getSynsets(key,SynsetType.ADVERB);
			    		  //System.out.print("WordForms"+"\t"+"TagCount"+"\t "+"   Sense Definition\n");
						  //System.out.println("---------"+" ***** "+"--------"+" ****** "+" ---------------------");
						  senses1=new ArrayList<String>();
					      for (Synset synset2 : synset) {
						    AdverbSynset adverbsynset=(AdverbSynset) synset2;
						    //to get actual word and its senses
						   // System.out.print(adverbsynset.getWordForms()[0] + "\t\t   " +adverbsynset.getTagCount(adverbsynset.getWordForms()[0]) +"\t\t   "+ adverbsynset.getDefinition()+"\n");
						    senses1.add(adverbsynset.getDefinition());
						    //**System.out.println("checking for adverb"+adverbsynset.getDefinition());
					      }
					      //System.out.println("--------"+"********"+"---------"+"********"+"------------------------");
					      context_word_sense.put(key, senses1);  //context word senses are stored in map.
					      //senses1.clear();
			    	  }
			    	  else if(value.equalsIgnoreCase("ADJECTIVE")){
			    		 // System.out.println("---- if context word is adjective ----");
			    		  synset=wdatabase.getSynsets(key,SynsetType.ADJECTIVE);
			    		 // System.out.print("WordForms"+"\t"+"TagCount"+"\t "+"   Sense Definition\n");
						//  System.out.println("---------"+" ***** "+"--------"+" ****** "+" ---------------------");
						  senses1=new ArrayList<String>();
					      for (Synset synset2 : synset) {
						    AdjectiveSynset adjectivesynset=(AdjectiveSynset) synset2;
						    //to get actual word and its senses
						  //  System.out.print(adjectivesynset.getWordForms()[0] + "\t\t   " +adjectivesynset.getTagCount(adjectivesynset.getWordForms()[0]) +"\t\t   "+ adjectivesynset.getDefinition()+"\n");
						    senses1.add(adjectivesynset.getDefinition());
						    //**System.out.println("checking  for adjective"+adjectivesynset.getDefinition());
					      }
					   //   System.out.println("--------"+"********"+"---------"+"********"+"------------------------");
					      context_word_sense.put(key, senses1);  //context word senses are stored in map.
					      //senses1.clear();
			    	  }
			    	  
			      } //outer loop ends.
			      final_sense=Classifier.score_count(senses, context_word_sense);
			      break;
				}
				}
				}
				//return (final_sense);
			else
				break;
		} //end of outer loop for NN Array. 
		i=0;
		senses.clear();
		for (String st1 : VB) {
			//System.out.println("string in verb list : "+st1);
			if(st1!=null){ 
				if(st1.equalsIgnoreCase(target) && count==0){
					//System.out.println("if st1==target in VB");
					//System.out.println("Printing the verb sysnset");
				   synset=wdatabase.getSynsets(st1,SynsetType.VERB);
				   count++;
				  // System.out.println("Total Number of senses : "+synset.length);
				   if(synset.length==1){
					   System.out.println("This word has single sense as a verb");
				       final_sense=((VerbSynset)synset[0]).getDefinition();
				      }
				   else{
					   System.out.println("target word is verb:-- "+st1);
					//senses of the tareget word if it is a verb   
				//   System.out.print("WordForms"+"\t"+"TagCount"+"\t "+"   Sense Definition\n");
				  // System.out.println("---------"+" ***** "+"--------"+" ****** "+" ---------------------");
				   for (Synset synset2 : synset) {
					VerbSynset verbsynset=(VerbSynset)synset2;
					//System.out.print(verbsynset.getWordForms()[0]+ "\t\t   " +verbsynset.getTagCount(verbsynset.getWordForms()[0]) + "\t\t   " + verbsynset.getDefinition()+"\n");
					senses.add(verbsynset.getDefinition());
				  }
				   //System.out.println("--------"+"*******"+"----------"+"********"+"------------------------");
				 
				   // Retrieving the senses of the context words and stored in a Map.
				      for(Map.Entry<String, String> ctx : contextword.entrySet()){    //outer loop starts
				    	  String key=ctx.getKey().toString();
				    	  String value=ctx.getValue().toString();
				    	 // senses1.clear();
				    	  if(value.equalsIgnoreCase("NOUN")){
				    		  synset=wdatabase.getSynsets(key,SynsetType.NOUN);
				    		 // System.out.print("WordForms"+"\t"+"TagCount"+"\t "+"   Sense Definition\n");
							 // System.out.println("---------"+" ***** "+"--------"+" ****** "+" ---------------------");
							  senses1=new ArrayList<String>();
						      for (Synset synset2 : synset) {
							    nounsynset=(NounSynset) synset2;
							    //to get actual word and its senses
							  //  System.out.print(nounsynset.getWordForms()[0] + "\t\t   " +nounsynset.getTagCount(nounsynset.getWordForms()[0]) +"\t\t   "+ nounsynset.getDefinition()+"\n");
							    senses1.add(nounsynset.getDefinition());
						      }
						    //  System.out.println("--------"+"********"+"---------"+"********"+"------------------------");
						      context_word_sense.put(key, senses1);  //context word senses are stored in map.
						      
				    	  }
				    	  else if(value.equalsIgnoreCase("VERB")){
				    		  synset=wdatabase.getSynsets(key,SynsetType.VERB);
				    		 // System.out.print("WordForms"+"\t"+"TagCount"+"\t "+"   Sense Definition\n");
							 // System.out.println("---------"+" ***** "+"--------"+" ****** "+" ---------------------");
							  senses1=new ArrayList<String>();
						      for (Synset synset2 : synset) {
							    VerbSynset verbsynset=(VerbSynset) synset2;
							    //to get actual word and its senses
							    //System.out.print(verbsynset.getWordForms()[0] + "\t\t   " +verbsynset.getTagCount(verbsynset.getWordForms()[0]) +"\t\t   "+ verbsynset.getDefinition()+"\n");
							    senses1.add(verbsynset.getDefinition());
							    //**System.out.println("hello "+verbsynset.getDefinition());
						      }
						    //  System.out.println("--------"+"********"+"---------"+"********"+"------------------------");
						      context_word_sense.put(key, senses1);  //context word senses are stored in map.
						      //senses1.clear();
				    	  }
				    	  else if(value.equalsIgnoreCase("ADVERB")){
				    		  synset=wdatabase.getSynsets(key,SynsetType.ADVERB);
				    		  //System.out.print("WordForms"+"\t"+"TagCount"+"\t "+"   Sense Definition\n");
							//  System.out.println("---------"+" ***** "+"--------"+" ****** "+" ---------------------");
							  senses1=new ArrayList<String>();
						      for (Synset synset2 : synset) {
							    AdverbSynset adverbsynset=(AdverbSynset) synset2;
							    //to get actual word and its senses
							   // System.out.print(adverbsynset.getWordForms()[0] + "\t\t   " +adverbsynset.getTagCount(adverbsynset.getWordForms()[0]) +"\t\t   "+ adverbsynset.getDefinition()+"\n");
							    senses1.add(adverbsynset.getDefinition());
						      }
						     // System.out.println("--------"+"********"+"---------"+"********"+"------------------------");
						      context_word_sense.put(key, senses1);  //context word senses are stored in map.
						      //senses1.clear();
				    	  }
				    	  else if(value.equalsIgnoreCase("ADJECTIVE")){
				    		  synset=wdatabase.getSynsets(key,SynsetType.ADJECTIVE);
				    		 // System.out.print("WordForms"+"\t"+"TagCount"+"\t "+"   Sense Definition\n");
							  //System.out.println("---------"+" ***** "+"--------"+" ****** "+" ---------------------");
							  senses1=new ArrayList<String>();
						      for (Synset synset2 : synset) {
							    AdjectiveSynset adjectivesynset=(AdjectiveSynset) synset2;
							    //to get actual word and its senses
							 //   System.out.print(adjectivesynset.getWordForms()[0] + "\t\t   " +adjectivesynset.getTagCount(adjectivesynset.getWordForms()[0]) +"\t\t   "+ adjectivesynset.getDefinition()+"\n");
							    senses1.add(adjectivesynset.getDefinition());
						      }
						     // System.out.println("--------"+"********"+"---------"+"********"+"------------------------");
						      context_word_sense.put(key, senses1);  //context word senses are stored in map.
						     // senses1.clear();
				    	  }
				    	  
				      } //outer loop ends.
				   final_sense=Classifier.score_count(senses, context_word_sense);
				   break;
				}
				}
				//return (final_sense);
		    }
			else
				break;
		} //end of outer loop : VB
		
		senses.clear();
		for (String st1 : ADJ) {
			if(st1!=null){
				if(st1.equalsIgnoreCase(target) && count==0){
					//System.out.println("if st1==target in NN");
					//System.out.println("Printing the Adjective sysnset");
				 synset=wdatabase.getSynsets(st1,SynsetType.ADJECTIVE);
				 count++;
				// System.out.println("Total Number of senses : "+synset.length);
				 if(synset.length==1){
					  System.out.println("This word has single sense as a adjective");
			    	  final_sense=((AdjectiveSynset)synset[0]).getDefinition();
			      }
				 else{
				 //System.out.print("WordForms"+"\t"+"TagCount"+"\t "+"   Sense Definition\n");
			     //System.out.println("---------"+" ***** "+"--------"+" ****** "+" ---------------------");
				  for (Synset synset2 : synset) {
					AdjectiveSynset adjsynset=(AdjectiveSynset)synset2;
					//System.out.println(adjsynset.getWordForms()[0]+ "\t\t   " +adjsynset.getTagCount(adjsynset.getWordForms()[0]) +  "\t\t   " + adjsynset.getDefinition()+"\n");
					senses.add(adjsynset.getDefinition());
				  }
				 // System.out.println("--------"+"*******"+"----------"+"********"+"------------------------");
				  
				// Retrieving the senses of the context words and stored in a Map.
			      for(Map.Entry<String, String> ctx : contextword.entrySet()){    //outer loop starts
			    	  String key=ctx.getKey().toString();
			    	  String value=ctx.getValue().toString();
			    	  //senses1.clear();
			    	  if(value.equalsIgnoreCase("NOUN")){
			    		  synset=wdatabase.getSynsets(key,SynsetType.NOUN);
			    		 // System.out.print("WordForms"+"\t"+"TagCount"+"\t "+"   Sense Definition\n");
						 // System.out.println("---------"+" ***** "+"--------"+" ****** "+" ---------------------");
						  senses1=new ArrayList<String>();
					      for (Synset synset2 : synset) {
						    nounsynset=(NounSynset) synset2;
						    //to get actual word and its senses
						    //System.out.print(nounsynset.getWordForms()[0] + "\t\t   " +nounsynset.getTagCount(nounsynset.getWordForms()[0]) +"\t\t   "+ nounsynset.getDefinition()+"\n");
						    senses1.add(nounsynset.getDefinition());
					      }
					     // System.out.println("--------"+"********"+"---------"+"********"+"------------------------");
					      context_word_sense.put(key, senses1);  //context word senses are stored in map.
					     // senses1.clear();
			    	  }
			    	  else if(value.equalsIgnoreCase("VERB")){
			    		  synset=wdatabase.getSynsets(key,SynsetType.VERB);
			    		  //System.out.print("WordForms"+"\t"+"TagCount"+"\t "+"   Sense Definition\n");
						//  System.out.println("---------"+" ***** "+"--------"+" ****** "+" ---------------------");
						  senses1=new ArrayList<String>();
					      for (Synset synset2 : synset) {
						    VerbSynset verbsynset=(VerbSynset) synset2;
						    //to get actual word and its senses
						   // System.out.print(verbsynset.getWordForms()[0] + "\t\t   " +verbsynset.getTagCount(verbsynset.getWordForms()[0]) +"\t\t   "+ verbsynset.getDefinition()+"\n");
						    senses1.add(verbsynset.getDefinition());
					      }
					     // System.out.println("--------"+"********"+"---------"+"********"+"------------------------");
					      context_word_sense.put(key, senses1);  //context word senses are stored in map.
					     // senses1.clear();
			    	  }
			    	  else if(value.equalsIgnoreCase("ADVERB")){
			    		  synset=wdatabase.getSynsets(key,SynsetType.ADVERB);
			    		//  System.out.print("WordForms"+"\t"+"TagCount"+"\t "+"   Sense Definition\n");
						//  System.out.println("---------"+" ***** "+"--------"+" ****** "+" ---------------------");
						  senses1=new ArrayList<String>();
					      for (Synset synset2 : synset) {
						    AdverbSynset adverbsynset=(AdverbSynset) synset2;
						    //to get actual word and its senses
						   // System.out.print(adverbsynset.getWordForms()[0] + "\t\t   " +adverbsynset.getTagCount(adverbsynset.getWordForms()[0]) +"\t\t   "+ adverbsynset.getDefinition()+"\n");
						    senses1.add(adverbsynset.getDefinition());
					      }
					      //System.out.println("--------"+"********"+"---------"+"********"+"------------------------");
					      context_word_sense.put(key, senses1);  //context word senses are stored in map.
					     // senses1.clear();
			    	  }
			    	  else if(value.equalsIgnoreCase("ADJECTIVE")){
			    		  synset=wdatabase.getSynsets(key,SynsetType.ADJECTIVE);
			    		  //System.out.print("WordForms"+"\t"+"TagCount"+"\t "+"   Sense Definition\n");
						  //System.out.println("---------"+" ***** "+"--------"+" ****** "+" ---------------------");
						  senses1=new ArrayList<String>();
					      for (Synset synset2 : synset) {
						    AdjectiveSynset adjectivesynset=(AdjectiveSynset) synset2;
						    //to get actual word and its senses
						   // System.out.print(adjectivesynset.getWordForms()[0] + "\t\t   " +adjectivesynset.getTagCount(adjectivesynset.getWordForms()[0]) +"\t\t   "+ adjectivesynset.getDefinition()+"\n");
						    senses1.add(adjectivesynset.getDefinition());
					      }
					      //System.out.println("--------"+"********"+"---------"+"********"+"------------------------");
					      context_word_sense.put(key, senses1);  //context word senses are stored in map.
					      //senses1.clear();
			    	  }
			    	  
			      } //outer loop ends.
			      final_sense=Classifier.score_count(senses, context_word_sense);
				  break;
				}
				}
				//return (final_sense);
		    }
			else
				break;
		} //end of outer loop : ADJ
		senses.clear();
		for (String st1 : ADV) {
			if(st1!=null){ 
				if(st1.equalsIgnoreCase(target) && count==0){
					//System.out.println("if st1==target in NN");
					//System.out.println("Printing the Adverb sysnset");
				  synset=wdatabase.getSynsets(st1,SynsetType.ADVERB);
				  count++;
				 // System.out.println("Total Number of senses : "+synset.length);
				  if(synset.length==1){
					  System.out.println("This word has single sense as a adverb");
			    	  final_sense=((AdverbSynset)synset[0]).getDefinition();
			      }
				  else{
				  //System.out.print("WordForms"+"\t"+"TagCount"+"\t "+"   Sense Definition\n");
				  //System.out.println("---------"+" ***** "+"--------"+" ****** "+" ---------------------");
				  for (Synset synset2 : synset) {
					AdverbSynset adverbsynset=(AdverbSynset)synset2;
					//System.out.println(adverbsynset.getWordForms()[0]+ "\t\t   " +adverbsynset.getTagCount(adverbsynset.getWordForms()[0]) +  "\t\t   " + adverbsynset.getDefinition()+"\n");
					senses.add(adverbsynset.getDefinition());
				  }
				 // System.out.println("--------"+"*******"+"----------"+"********"+"------------------------");
				  
				// Retrieving the senses of the context words and stored in a Map.
			      for(Map.Entry<String, String> ctx : contextword.entrySet()){    //outer loop starts
			    	  String key=ctx.getKey().toString();
			    	  String value=ctx.getValue().toString();
			    	  //senses1.clear();
			    	  if(value.equalsIgnoreCase("NOUN")){
			    		  synset=wdatabase.getSynsets(key,SynsetType.NOUN);
			    		  //System.out.print("WordForms"+"\t"+"TagCount"+"\t "+"   Sense Definition\n");
						 // System.out.println("---------"+" ***** "+"--------"+" ****** "+" ---------------------");
						  senses1=new ArrayList<String>();
					      for (Synset synset2 : synset) {
						    nounsynset=(NounSynset) synset2;
						    //to get actual word and its senses
						   // System.out.print(nounsynset.getWordForms()[0] + "\t\t   " +nounsynset.getTagCount(nounsynset.getWordForms()[0]) +"\t\t   "+ nounsynset.getDefinition()+"\n");
						    senses1.add(nounsynset.getDefinition());
					      }
					     // System.out.println("--------"+"********"+"---------"+"********"+"------------------------");
					      context_word_sense.put(key, senses1);  //context word senses are stored in map.
					      //senses1.clear();
			    	  }
			    	  else if(value.equalsIgnoreCase("VERB")){
			    		  synset=wdatabase.getSynsets(key,SynsetType.VERB);
			    		  //System.out.print("WordForms"+"\t"+"TagCount"+"\t "+"   Sense Definition\n");
						//  System.out.println("---------"+" ***** "+"--------"+" ****** "+" ---------------------");
						  senses1=new ArrayList<String>();
					      for (Synset synset2 : synset) {
						    VerbSynset verbsynset=(VerbSynset) synset2;
						    //to get actual word and its senses
						   // System.out.print(verbsynset.getWordForms()[0] + "\t\t   " +verbsynset.getTagCount(verbsynset.getWordForms()[0]) +"\t\t   "+ verbsynset.getDefinition()+"\n");
						    senses1.add(verbsynset.getDefinition());
					      }
					     // System.out.println("--------"+"********"+"---------"+"********"+"------------------------");
					      context_word_sense.put(key, senses1);  //context word senses are stored in map.
					      //senses1.clear();
			    	  }
			    	  else if(value.equalsIgnoreCase("ADVERB")){
			    		  synset=wdatabase.getSynsets(key,SynsetType.ADVERB);
			    		  //System.out.print("WordForms"+"\t"+"TagCount"+"\t "+"   Sense Definition\n");
						  //System.out.println("---------"+" ***** "+"--------"+" ****** "+" ---------------------");
						  senses1=new ArrayList<String>();
					      for (Synset synset2 : synset) {
						    AdverbSynset adverbsynset=(AdverbSynset) synset2;
						    //to get actual word and its senses
						    //System.out.print(adverbsynset.getWordForms()[0] + "\t\t   " +adverbsynset.getTagCount(adverbsynset.getWordForms()[0]) +"\t\t   "+ adverbsynset.getDefinition()+"\n");
						    senses1.add(adverbsynset.getDefinition());
					      }
					     // System.out.println("--------"+"********"+"---------"+"********"+"------------------------");
					      context_word_sense.put(key, senses1);  //context word senses are stored in map.
					      //senses1.clear();
			    	  }
			    	  else if(value.equalsIgnoreCase("ADJECTIVE")){
			    		  synset=wdatabase.getSynsets(key,SynsetType.ADJECTIVE);
			    		 // System.out.print("WordForms"+"\t"+"TagCount"+"\t "+"   Sense Definition\n");
						 // System.out.println("---------"+" ***** "+"--------"+" ****** "+" ---------------------");
						  senses1=new ArrayList<String>();
					      for (Synset synset2 : synset) {
						    AdjectiveSynset adjectivesynset=(AdjectiveSynset) synset2;
						    //to get actual word and its senses
						   // System.out.print(adjectivesynset.getWordForms()[0] + "\t\t   " +adjectivesynset.getTagCount(adjectivesynset.getWordForms()[0]) +"\t\t   "+ adjectivesynset.getDefinition()+"\n");
						    senses1.add(adjectivesynset.getDefinition());
					      }
					     // System.out.println("--------"+"********"+"---------"+"********"+"------------------------");
					      context_word_sense.put(key, senses1);  //context word senses are stored in map.
					      //senses1.clear();
			    	  }
			    	  
			      } //outer loop ends.
			     final_sense=Classifier.score_count(senses, context_word_sense);
			   break;
				}
				}
			 }
			else
				break;
		} 
		
		//end of outer loop : VB
		//String base[]=id.getBaseFormCandidates("wolves", SynsetType.NOUN);
		//System.out.println("base form.....");
		/*for (String string : base) {
			System.out.println(string);
		}*/
		return final_sense;
 }
	

}
