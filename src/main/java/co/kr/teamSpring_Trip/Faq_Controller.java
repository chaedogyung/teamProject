package co.kr.teamSpring_Trip;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import java.util.*;//HashMap 사용
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.ibatis.session.SqlSession;//Mybatis 사용 private SqlSession sqlSession(변수 소문자로 둔다)
import model.faq.FaqDto;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import javax.naming.NamingException;
import org.springframework.web.servlet.ModelAndView;


//Front Controller(org.springframework.web.servlet.DispatcherServlet)
//                (org.springframework.web.servlet.DispatcherServlet)
//Controller(컨트롤러) : Action에 해당

@Controller
public class Faq_Controller {

	@Autowired//setter가 자동
	private SqlSession sqlSession;//변수 MyBatis 사용 




	//list 리스트
	@RequestMapping("faq_list_.do")
	public String listfaq(Model model, String pageNum)
			throws IOException,NamingException{

		if(pageNum==null) {pageNum="1";}

		int pageSize=10;
		int currentPage=Integer.parseInt(pageNum);
		int startRow=(currentPage-1)*pageSize+1;//한 페이지의 첫번째 row 구한다
		int endRow=currentPage*pageSize;//한페이지의 마지막 row를 구한다

		int count=0;//총 글 수 넣으려고
		int pageBlock=10;//블럭당 10개 페이지

		count=sqlSession.selectOne("faq.selectCount");//총 글 수 얻기

		int number=count-(currentPage-1)*pageSize;//글번호를 역순으로
			
		int pageCount=count/pageSize+(count%pageSize==0?0:1);//총 페이지 수 구하기
		int startPage=(currentPage/10)*10+1;//시작페이지
		//                
		int endPage=startPage+pageBlock-1;
		//             1+10-1=10 end페이지	
				
		HashMap<String,Integer> map=new HashMap<String,Integer>();
		map.put("start", startRow-1);//시작위치, mysql은 0부터 시작]
		map.put("cnt", pageSize);//글 갯수 (10개씩)

		List<FaqDto> list=sqlSession.selectList("faq.selectList",map);

		model.addAttribute("currentPage",currentPage);
		model.addAttribute("startRow",startRow);
		model.addAttribute("endRow",endRow);

		model.addAttribute("pageBlock",pageBlock);
		model.addAttribute("pageCount",pageCount);

		model.addAttribute("startPage",startPage);
		model.addAttribute("endPage",endPage);

		model.addAttribute("count",count);
		model.addAttribute("pageSize",pageSize);

		model.addAttribute("number",number);
		model.addAttribute("list",list);//출력할 데이터

		return ".main.faq.faq_list_";//뷰 리턴
	}

	//조횟수 증가, 글내용보기
	@RequestMapping("faq_content_.do")
	public String content(Model model,String num, String pageNum)
	throws IOException,NamingException{
		int num1=Integer.parseInt(num);
		
		//글 내용 보기
		FaqDto faqDto=sqlSession.selectOne("faq.getFaq",num1);
		
		String content=faqDto.getContent();
		content=content.replace("\n", "<br>");
		model.addAttribute("content",content);
		
		model.addAttribute("num",num1);
		model.addAttribute("pageNum",pageNum);
		model.addAttribute("faqDto",faqDto);
		
		return ".main.faq.faq_content_";//뷰 리턴, views/faq/content.jsp
	}

	
	

}//class
