package dev.main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dev.polygon.DevPolyCommon;
import dev.polygon.DevPolygon;
import dev.polygon.PolygonJava;

public class DevPolygonMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PolygonJava c_Po = new PolygonJava();
		DevPolygon c_po2 = null;		
		
		DevPolyCommon c_Com = new DevPolyCommon();
		c_Com.setMapVertexInfo();
			
		String mResult = "";
		
		int nMinX = 0;
		int nMaxX = 0;
		int nMinY = 0;
		int nMaxY = 0;
		
		int nTotal = 0;
		double bstTime = System.currentTimeMillis();
		
		BufferedReader br = null;
		
		try {
			br = new BufferedReader(new FileReader("D:\\20140817좌표데이터.csv"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String strTemp = null;		
		
//		for(int jk = 0;jk<10000;jk++){
		try {
			while((strTemp=br.readLine())!=null){
				
				String newAdmCode = "";
				String oldAdmCode = "";
				
				String[] arrTemp = strTemp.split(",",-1);
			
//			int nHomeX = -35837 +jk;
//			int nHomeY = 588907 +jk;
//				122342,474625
				/*
				 * 확인해야 할 행정코드
				 */
//				4427031022
//				4427032025
				
				int nHomeX = 0;
				int nHomeY = 0;
				try {
					nHomeX = Integer.valueOf(arrTemp[0]);
				} catch(Exception e){}
				
				try {
					nHomeY = Integer.valueOf(arrTemp[1]);
				} catch(Exception e){}
				
				int i =0;
				
				String mHomeX = String.valueOf(nHomeX);
				String mHomeY = String.valueOf(nHomeY);
				
								
				try {
					HashMap<String, HashMap<String, String>> mHashMap = DevPolyCommon.m_FirstIdxMap.get(mHomeX.substring(0, mHomeX.length()-4));
					HashMap<String, String> mHash = mHashMap.get(mHomeY.substring(0, mHomeY.length()-4));
					
					
					
					for(Map.Entry<String, String> entry : mHash.entrySet()){
						String mSquareXY = entry.getKey();
						String mAdmCode = entry.getValue();
	
						String[] arrSquareXY = mSquareXY.split("_",-1);
						
						nMinX = Integer.valueOf(arrSquareXY[1]);
						nMaxX = Integer.valueOf(arrSquareXY[0]);
						nMaxY = Integer.valueOf(arrSquareXY[2]);
						nMinY = Integer.valueOf(arrSquareXY[3]);
												
						/*
						 * Square에 들어가는 값을 확인
						 */
						if(nHomeX>=nMinX && nHomeX<=nMaxX && nHomeY>=nMinY && nHomeY<=nMaxY){
//							System.out.println(nMinX +"_" + nMaxX +"_" + nMinY + "_" + nMaxY +"_");
							ArrayList<int[]> m_ArrX = DevPolyCommon.m_GeoVertexXMap.get(mAdmCode);
							ArrayList<int[]> m_ArrY = DevPolyCommon.m_GeoVertexYMap.get(mAdmCode);
							
							if(m_ArrX.size()!=m_ArrY.size()) {
								System.out.println("Data Error");
								break;
							}
							
							for(int j=0;j<m_ArrX.size();j++){
							
								int[] arrPolyX = m_ArrX.get(j);
								int[] arrPolyY = m_ArrY.get(j);
								
								/*
								 * 
								 */
								boolean bResult = c_Po.CheckPolygon(arrPolyX, arrPolyY, nHomeX, nHomeY);
								if(bResult) {
	//								System.out.println("NEW LOGIC검색 결과 : " +bResult);
									System.out.println("NEW LOGIC행정 코드 : " + mAdmCode);
									newAdmCode = mAdmCode;
									nTotal++;
									break;
								}
							}
						}	
						
						i++;
						
					}
				} catch(Exception e){
//					System.out.println(mHomeX);
				}
			
			
			
			
//			for(Map.Entry<String, String> entry : DevPolyCommon.m_GeoVertexSquareMap.entrySet()){
//				String mSquareXY = entry.getKey();
//				String mAdmCode = entry.getValue();
//
//				String[] arrSquareXY = mSquareXY.split("_",-1);
//				
//				nMinX = Integer.valueOf(arrSquareXY[1]);
//				nMaxX = Integer.valueOf(arrSquareXY[0]);
//				nMaxY = Integer.valueOf(arrSquareXY[2]);
//				nMinY = Integer.valueOf(arrSquareXY[3]);
//				
//				/*
//				 * Square에 들어가는 값을 확인
//				 */
//				if(nHomeX>=nMinX && nHomeX<=nMaxX && nHomeY>=nMinY && nHomeY<=nMaxY){
////					System.out.println(nMinX +"_" + nMaxX +"_" + nMinY + "_" + nMaxY +"_");
//					ArrayList<int[]> m_ArrX = DevPolyCommon.m_GeoVertexXMap.get(mAdmCode);
//					ArrayList<int[]> m_ArrY = DevPolyCommon.m_GeoVertexYMap.get(mAdmCode);
//					
//					if(m_ArrX.size()!=m_ArrY.size()) {
//						System.out.println("Data Error");
//						break;
//					}
//					
//					for(int j=0;j<m_ArrX.size();j++){
//						int[] arrPolyX = m_ArrX.get(j);
//						int[] arrPolyY = m_ArrY.get(j);
//						
//						/*
//						 * 
//						 */
//						boolean bResult = c_Po.CheckPolygon(arrPolyX, arrPolyY, nHomeX, nHomeY);
//						if(bResult) {
//	//						System.out.println("OLD LOGIC검색 결과 : " +bResult);
////							System.out.println("OLD LOGIC행정 코드 : " + mAdmCode);
//							oldAdmCode = mAdmCode;
//							break;
//						}
//					}
//				}			
//				
//				i++;
//			}
			
			
//				System.out.println("검색 횟수: " +i);
				
				if(!newAdmCode.equals(oldAdmCode)){
					System.out.println(newAdmCode + ":" + oldAdmCode);
				}
			
			}
			
			br.close();
		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		double bedTime = System.currentTimeMillis();
		System.out.println("검색 시간 : " + (bedTime-bstTime));
		
		System.out.println(nTotal);

	}

}
