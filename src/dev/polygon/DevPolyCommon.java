package dev.polygon;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

import com.mongodb.BasicDBList;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

public class DevPolyCommon {

	//Vertex 전체 정보 MAP
	public static HashMap<String, ArrayList<int[]>> m_GeoVertexXMap = new HashMap<String, ArrayList<int[]>>();
	public static HashMap<String, ArrayList<int[]>> m_GeoVertexYMap = new HashMap<String, ArrayList<int[]>>();
	
	//Vertex 좌하, 우상 정보 MAP
	public static HashMap<String, String> m_GeoVertexSquareMap = new HashMap<String, String>();
	
	//Vertex INDEX - X좌표 기준
//	public static HashMap<String, HashMap<String, String>> m_FirstIdxMap= new HashMap<String, HashMap<String, String>>();
	
	//Vertex INDEX - X좌표 -> Y좌표 -> Vertex
	public static HashMap<String, HashMap<String, HashMap<String, String>>> m_FirstIdxMap = new HashMap<String, HashMap<String, HashMap<String, String>>>();
	
	
	Mongo mMongo = null;
	DB mDB = null;
	
	/*
	 * MongoDB 에서 읽어서 Vertex정보를 메모리에 저장 한다.
	 * MinMax 정보
	 * Vertex X
	 * Vertex Y
	 * 
	 */
	public void setMapVertexInfo(){
		
		try {
			mMongo = new Mongo("10.10.82.179");
			mDB = mMongo.getDB("SNDB");
			
			DBCollection mColl = mDB.getCollection("SNAdmVertexAllInfo");
			
			DBCursor mCur = mColl.find();
			
			int nCnt = 0;
			
			double bstTime = System.currentTimeMillis();
			
			while(mCur.hasNext()){
				DBObject mResult = mCur.next();
				
				StringBuffer sbRangeValue = new StringBuffer();
				
				String strMaxX = "";
				String strMinX = "";
				String strMaxY = "";
				String strMinY = "";
				
				//Poly_range value
				DBObject mPolyRange = (DBObject)mResult.get("POLY_RANGE");
				
				strMaxX = String.valueOf(mPolyRange.get("MAX_X"));
				strMinX = String.valueOf(mPolyRange.get("MIN_X"));
				strMaxY = String.valueOf(mPolyRange.get("MAX_Y"));
				strMinY = String.valueOf(mPolyRange.get("MIN_Y"));
				
				sbRangeValue.append(strMaxX);
				sbRangeValue.append("_");
				sbRangeValue.append(strMinX);
				sbRangeValue.append("_");
				sbRangeValue.append(strMaxY);
				sbRangeValue.append("_");
				sbRangeValue.append(strMinY);
				
				String strAdmCode = (String)mResult.get("ADMCODE");				
				
				String minIdxX = strMinX.substring(0, strMinX.length()-4);
				String maxIdxX = strMaxX.substring(0, strMaxX.length()-4);
				
				String minIdxY = strMinY.substring(0, strMinY.length()-4);
				String maxIdxY = strMaxY.substring(0, strMaxY.length()-4);
				
				if(minIdxX.equals("")) minIdxX = "0";
				if(maxIdxX.equals("")) maxIdxX = "0";
				if(minIdxY.equals("")) minIdxY = "0";
				if(maxIdxY.equals("")) maxIdxY = "0";				
				
//				System.out.println(minIdxX + "_" +maxIdxX+"_"+ minIdxY+"_"+maxIdxY);
								
				HashMap<String, HashMap<String, String>> mHash = new HashMap<String, HashMap<String, String>>();
				//First INDEX
				if(minIdxX.equals(maxIdxX)){
					
					if(m_FirstIdxMap.get(minIdxX)!=null){
						mHash = m_FirstIdxMap.get(minIdxX);						
					} 
					
					setIndex(minIdxY, maxIdxY, sbRangeValue.toString(), strAdmCode, mHash);					
					m_FirstIdxMap.put(minIdxX, mHash);
					
				} else {
					//최대 최소 값의 앞자리 두 숫자가 차이가 클때
					int nMinX = 0;
					int nMaxX = 0;
				
					nMinX = Integer.valueOf(minIdxX);
					nMaxX = Integer.valueOf(maxIdxX);
					
					while(nMinX <=nMaxX){
						mHash = new HashMap<String, HashMap<String, String>>();
						
						minIdxX = String.valueOf(nMinX);
						
						if(m_FirstIdxMap.get(minIdxX)!=null){
							mHash = m_FirstIdxMap.get(minIdxX);						
						} 
					
						setIndex(minIdxY, maxIdxY, sbRangeValue.toString(), strAdmCode, mHash);					
						m_FirstIdxMap.put(minIdxX, mHash);
						
						nMinX++;
					}					
					
//					if(m_FirstIdxMap.get(maxIdxX)!=null){
//						mHash = m_FirstIdxMap.get(maxIdxX);						
//					} 
//					
//					setIndex(minIdxY, maxIdxY, sbRangeValue.toString(), strAdmCode, mHash);					
//					m_FirstIdxMap.put(maxIdxX, mHash);
				}
				
				
				//MIN-MAX MAP 저장
				m_GeoVertexSquareMap.put(sbRangeValue.toString(), strAdmCode);
				
				DBObject mPolyXY = (DBObject)mResult.get("PoiXY");
				BasicDBList bListPolyX = (BasicDBList) mPolyXY.get("PolygonX");
				BasicDBList bListPolyY = (BasicDBList)mPolyXY.get("PolygonY");
				
				int[] arrPolyX = new int[bListPolyX.size()];
				int[] arrPolyY = new int[bListPolyY.size()];
				
				for(int i=0;i<bListPolyX.size();i++){
					arrPolyX[i] = (Integer) bListPolyX.get(i);
					arrPolyY[i] = (Integer) bListPolyY.get(i);
				}
				
				ArrayList<int[]> m_ArrX = new ArrayList<int[]>();
				ArrayList<int[]> m_ArrY = new ArrayList<int[]>();
				
				//Polygon Info X
				if(m_GeoVertexXMap.get(strAdmCode)!=null){
					m_ArrX = m_GeoVertexXMap.get(strAdmCode);					
				}
				
				m_ArrX.add(arrPolyX);
				m_GeoVertexXMap.put(strAdmCode, m_ArrX);
				
				//Polygon Info Y;
				if(m_GeoVertexYMap.get(strAdmCode)!=null){
					m_ArrY = m_GeoVertexYMap.get(strAdmCode);					
				}
				
				m_ArrY.add(arrPolyY);
				m_GeoVertexYMap.put(strAdmCode, m_ArrY);								
				
//				if(nCnt%5000==0) System.out.println(nCnt);
//				nCnt++;
				
			}
			
			double bedTime = System.currentTimeMillis();
			System.out.println(bedTime - bstTime);
			
//			System.out.println(nCnt);
			
			mMongo.close();			
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	/*
	 * Y Idx를 가지고 Second IDX를 만듬
	 */	
	public HashMap setFirstSecIndex(HashMap<String, HashMap<String, String>> mHash, String minIdxY, String maxIdxY){
		//데이터가 있다.		
		
		if(mHash.size()>0){
			
		} else {
			
		}
		
		return mHash;
	}
	
	/*
	 * Set LastIdx Data
	 */
	public void setIndex(String minIdxY, String maxIdxY, String strKey, String strValue, HashMap<String, HashMap<String, String>> mHash){
		HashMap<String, String> mYHash = new HashMap<String, String>();
		
		if(minIdxY.equals(maxIdxY)){
			
			if(mHash.get(minIdxY)!=null){
				mYHash = mHash.get(minIdxY);
			}
			
			mYHash.put(strKey, strValue);
			mHash.put(minIdxY, mYHash);
		} else {
			/*
			 * MinY, MaxY, 앞자리 2자리가 다르면 각자 저장
			 */
			int nMinY = Integer.valueOf(minIdxY);
			int nMaxY = Integer.valueOf(maxIdxY);
			
			while(nMinY<=nMaxY){
				mYHash = new HashMap<String, String>();
				minIdxY = String.valueOf(nMinY);
				
				if(mHash.get(minIdxY)!=null){
					mYHash = mHash.get(minIdxY);
				}
				
				mYHash.put(strKey, strValue);
				mHash.put(minIdxY, mYHash);
				
				nMinY++;
			}
			
			
//			if(mHash.get(maxIdxY)!=null){
//				mYHash = mHash.get(maxIdxY);
//			}
//			
//			mYHash.put(strKey, strValue);
//			mHash.put(maxIdxY, mYHash);
		}		
	}
}
