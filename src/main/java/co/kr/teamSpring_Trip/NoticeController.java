package co.kr.teamSpring_Trip;

import org.springframework.stereotype.Controller; //컨트롤러
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import model.notice.NoticeDto; //notice DB

import org.springframework.beans.factory.annotation.Autowired; //자동 Setter 작업

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.SqlSession;
import org.springframework.ui.Model;

@Controller
public class NoticeController {
	
	@Autowired
	private SqlSession sqlSession;

//____________________________________________________________________________________
	//공지사항 등록하기 (글 쓰기) , noticeWriteForm.jsp
	@RequestMapping("/noticeWriteForm.do")
	public String noticePlusForm(Model model, String n_num){
		
		n_num = "0";
		//원글 
		//매개변수를  'int n_num'로 정의하고,
		//addAttribute("n_num", n_num); 로 사용해도 똑같은 결과가 나오는 지 테스트[v]
		
		//View에서 사용할 속성
		model.addAttribute("n_num", new Integer(n_num)); //*** int로 변수생성 후 Integer()
//		model.addAttribute("n_num", n_num);
		
		return ".main.notice.noticeWriteForm"; //views/notice/noticeWriteForm.jsp
	}
	
//____________________________________________________________________________________
	//DB에 글쓰기 , noticeWritePro.jsp
	@RequestMapping(value = "noticeWritePro.do", method = RequestMethod.POST)
	public String noticePlusProc(@ModelAttribute("noticeDto") NoticeDto noticeDto,
			HttpServletRequest request) throws IOException, NamingException{
		
		//최대 글번호
		int maxNum = 0;
		maxNum = sqlSession.selectOne("notice.noticeMaxNum");
		
		//아이피 구하기
		String ip = request.getRemoteAddr();
		noticeDto.setIp(ip); 
		
		//insert
		sqlSession.insert("notice.writeNotice", noticeDto);
		
		return "redirect:noticeList.do";
	}
//____________________________________________________________________________________	
	//리스트 , noticeList.jsp
	@RequestMapping("noticeList.do")
	public String noticeView(Model model, String pageNum)
		throws IOException, NamingException{
		
		if(pageNum == null) {pageNum = "1";}
		
		int pageSize = 10;
		int currentPage = Integer.parseInt(pageNum);
		int startRow = (currentPage - 1) * pageSize + 1; //한 페이지의 첫번째 row 구하기
		int endRow = currentPage * pageSize; //한 페이지의 마지막 row 구하기
		
		int count = 0; //총 글 수 넣기 위해
		int pageBlock = 10; //블럭당 10개 패이지로 묶기
		
		count = sqlSession.selectOne("notice.selectCount"); //총 글 수 얻기
		//selectOne은 XML에서 해당 쿼리수행한것을 가져오기 *****
		
		int number = count - (currentPage - 1) * pageSize; //글번호를 역순으로 하기
		
		HashMap <String, Integer> map = new HashMap<String, Integer>(); //HashMap 핵심
		map.put("start", startRow - 1); //시작 위치, MySql은 0부터 시작
		map.put("cnt", pageSize); //글 갯수 ( 10개씩 )
		int pageCount = count / pageSize + (count % pageSize == 0 ? 0 : 1); //총 페이지 수
//							    몫				꽁다리 레코드 수
		int startPage = (currentPage / 10) * 10 + 1; // 시작페이지
		int endPage = startPage + pageBlock - 1;
		
		
		List <NoticeDto> list = sqlSession.selectList("notice.viewNotice", map);
		//selectList("XML쿼리경로",map); *****
		
		model.addAttribute("pageSize",pageSize);
		model.addAttribute("currentPage",currentPage);
		
		model.addAttribute("startRow",startRow);
		model.addAttribute("endRow",endRow);
		
		model.addAttribute("count",count);
		model.addAttribute("pageBlock",pageBlock);
		
		model.addAttribute("number",number);
		model.addAttribute("pageCount",pageCount);
		
		model.addAttribute("startPage",startPage);
		model.addAttribute("endPage",endPage);
		
		model.addAttribute("list",list); //출력할 데이터
		
		/* <이 로직은 나중에 추가>
		 * if(count > 0){//글이 존재하면 noticeList = dao.noticeGetList(startRow, pageSize);
		 * //dao메서드 호출하고 결과 받는다 }else{//글이 없으면 noticeList = Collections.EMPTY_LIST;
		 * //비어있는는 뜻 request.setAttribute("noticeList", noticeList);
		 */
		
		return ".main.notice.noticeList"; //뷰 리턴
	}
	
//____________________________________________________________________________________
	//글 내용 보기 + 조회수 증가 , noticeContent.jsp
	@RequestMapping("noticeContent.do")
	public String noticeText(Model model, String n_num, String pageNum)
	throws IOException, NamingException{
		
		int numCount = Integer.parseInt(n_num);
		sqlSession.update("notice.noticeReadCount", numCount);
				
		NoticeDto noticeDto = sqlSession.selectOne("notice.contentView", numCount);
		
		String n_content = noticeDto.getN_content();
		n_content = n_content.replace("\n", "<br>");
		
		model.addAttribute("n_content", n_content);
		model.addAttribute("n_num", numCount); //String num은 조회수 int numCount
		
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("noticeDto", noticeDto);
		
		return ".main.notice.noticeContent";
	}
//____________________________________________________________________________________
	//글 수정 , noticeUpdateForm.jsp
	@RequestMapping("noticeUpdateForm.do")
	public ModelAndView updateFrom(String n_num, String pageNum)
	throws IOException, NamingException{
		
		int num1 = Integer.parseInt(n_num);
		NoticeDto noticeDto = sqlSession.selectOne("notice.getNotice",num1);
		
		ModelAndView mv = new ModelAndView();
		mv.addObject("pageNum", pageNum);
		mv.addObject("noticeDto", noticeDto);
		mv.setViewName(".main.notice.noticeUpdateForm"); //뷰
		
		return mv;
	}
	
	
	
//____________________________________________________________________________________
	//DB에 글 수정 , noticeUpdatePro.jsp
	@RequestMapping(value = "noticeUpdatePro.do", method = RequestMethod.POST)
	public ModelAndView updatePro(NoticeDto noticeDto, String pageNum)
	throws IOException, NamingException{
		sqlSession.update("notice.updateNoticeProc", noticeDto);
		
		ModelAndView mv = new ModelAndView();
		mv.addObject("pageNum", pageNum);
		mv.setViewName("redirect:noticeList.do");
		
		return mv;
		
	}
	
	
//____________________________________________________________________________________
	//글 삭제 , noticeDeleteForm.jsp
	
	
//____________________________________________________________________________________
	//DB에 글 삭제 , noticeDeletePro.jsp
	
//	  @RequestMapping("noticeDeletePro.do") 
	  @RequestMapping("deleteNotice.do") 
	  public String deleteNotice(Model model, String n_num, String pageNum) { 
		  sqlSession.delete("notice.deleteNoticeProc", new Integer(n_num));
	  
	  model.addAttribute("pageNum", pageNum);
	  
	  return "redirect:noticeList.do";
	  
	  }
	 
}//class end