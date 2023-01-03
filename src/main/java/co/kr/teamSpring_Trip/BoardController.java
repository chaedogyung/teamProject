package co.kr.teamSpring_Trip;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import java.util.*;////HashMap 사용
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.ibatis.session.SqlSession;//Mybatis 사용 private SqlSession sqlSession(변수 소문자로 둔다)
import model.board.BoardDto;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import javax.naming.NamingException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class BoardController {
   
   @Autowired //setter가 자동
   private SqlSession sqlSession;
   
   //글쓰기 폼, 답글, 원글 writeForm
   @RequestMapping("/writeForm.do")
   public String writeF(Model model,String num,String ref, 
         String re_step,String re_level,String pageNum) {
      
      if(num==null) {//���� ����
         num="0";//글 번호
         ref="1";//글 그룹
         re_step="0";// 글 순서
         re_level="0";//글 계층
      }
      
      model.addAttribute("pageNum",pageNum);//request.setAttribute()
      model.addAttribute("num",new Integer(num));
      model.addAttribute("ref",new Integer(ref));
      model.addAttribute("re_step",new Integer(re_step));
      model.addAttribute("re_level",new Integer(re_level));
      // Model addAttribute(String name, Object value)
      //value객체를 name이름으로 추가한다, 뷰 코드에서는 name으로 
      //지정한 이름을 통해서 value를 사용한다.
      
      //return "board/writeForm";//�� �̸� ���� views/ writeForm.jsp
      return ".main.board.writeForm";//�� �̸� ���� views/ writeForm.jsp
   }
   
   //DB에 글쓰기
   @RequestMapping(value="writePro.do",method=RequestMethod.POST)//뷰에 form 태그 안에도 method를 post로 설정 해준다
   public String writePro(@ModelAttribute("boardDto") BoardDto boardDto,
         HttpServletRequest request) throws IOException,NamingException{
         //HttpServletRequest : 사용자가 서버쪽으로 데이터를 요청할 때, 요청에 대한 기능과 속성들을 가지고 있는 객체다
      int maxNum=0;//최대 글번호
      if(sqlSession.selectOne("board.numMax") != null) {
    	//최대 글번호가 null이 아니면=최대글번호가 있으면
         maxNum=sqlSession.selectOne("board.numMax");
      }
      
      if(maxNum != 0) {//최대 글번호가 0이 아니면 
         maxNum=maxNum+1;//최대 글번호 +1로 글작성, 답글 그룹으로 하려고
      }else {
         maxNum=1;//글번호 1부터 작성(처음글이면), 답글 그룹으로 사용하려고
      }
      
      String ip=request.getRemoteAddr();//ip구한다
      boardDto.setIp(ip);//setter 작업
      
      if(boardDto.getNum() != 0) {//����̸�
         //��� ���� �ֱ� ��ġ Ȯ��
         sqlSession.update("board.reStep",boardDto);
         boardDto.setRe_step(boardDto.getRe_step() + 1);//�ۼ���+1
         boardDto.setRe_level(boardDto.getRe_level() + 1);//�� ���� +1
      }else {//�����϶�
         boardDto.setRef(new Integer(maxNum));//�� �׷�
         boardDto.setRe_step(new Integer(0));// �� ���� 0
         boardDto.setRe_level(new Integer(0));//�� ���� 0
      }
      
      sqlSession.insert("board.insertBoard",boardDto);//insert
      
     // return "redirect:list.do";//response.sendRedirect("lsit.jsp)�� ����
        return ".main.board.writeForm";//�� �̸� ���� views/ writeForm.jsp
   }
   
   @RequestMapping("list.do")
   public String listboard(Model model, String pageNum) 
         throws IOException,NamingException{
      
      if(pageNum==null) {pageNum="1";}
      
      int pageSize=10;
      int currentPage=Integer.parseInt(pageNum);
      int startRow=(currentPage-1)*pageSize+1;//한 페이지의 첫번째 row 구하기
      int endRow=currentPage*pageSize;//한 페이지의 마지막 row 구하기
      
      int count=0; //총 글 수 넣기 위해
      int pageBlock=10;//블럭당 10개 패이지로 묶기
      
      count=sqlSession.selectOne("board.selectCount");//총 글 수 얻기
      //selectOne은 XML에서 해당 쿼리수행한것을 가져오기 *****
      
      int number=count-(currentPage-1)*pageSize;//글번호를 역순으로 하기 
      //int pageCount=count/pageSize+(count%pageSize==0?0:1);//�� �������� ���ϱ�
      
      HashMap<String,Integer> map=new HashMap<String,Integer>();
      map.put("start", startRow-1);//시작 위치, MySql은 0부터 시작
      map.put("cnt",pageSize);//글 갯수 ( 10개씩 )
      
      
      int pageCount=count/pageSize+(count%pageSize==0?0:1);//총 페이지 수
      //                 몫				꽁다리 레코드 수
      int startPage=(currentPage/10)*10+1;// 시작페이지
      //                
      int endPage=startPage+pageBlock-1;
      //             1+10-1=10 end페이지
      
      
      List<BoardDto> list=sqlSession.selectList("board.selectList",map);
      
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
      
      //return "board/list";//뷰 리턴
      return ".main.board.list";//뷰 리턴
   }
   
   //글 내용 보기 + 조회수 증가, content.jsp
   @RequestMapping("content.do")
   public String content(Model model,String num,String pageNum)
   throws IOException,NamingException{
	   int num1=Integer.parseInt(num);
	   sqlSession.update("board.readCount",num1);//��ȸ�� ����
	   
	   BoardDto boardDto=sqlSession.selectOne("board.getBoard",num1);
	   
	   String content=boardDto.getContent();
	   content=content.replace("\n","<br>");
	   model.addAttribute("content",content);
	   
	   model.addAttribute("num",num1);
	   model.addAttribute("pageNum",pageNum);
	   model.addAttribute("boardDto",boardDto);
	   
	   //return "board/content";//뷰리턴
	   return ".main.board.content";//뷰리턴
	   
   }
   
   //글 수정 , UpdateForm.jsp
   @RequestMapping("updateForm.do")
   public ModelAndView updateForm(String num, String pageNum)
   throws IOException,NamingException{
	   int num1=Integer.parseInt(num);
	   BoardDto boardDto=sqlSession.selectOne("board.getBoard",num1);
	   
	   ModelAndView mv=new ModelAndView();
	   mv.addObject("pageNum",pageNum);
	   mv.addObject("boardDto",boardDto);
	   //mv.setViewName("board/updateForm");//뷰
	   mv.setViewName(".main.board.updateForm");//뷰
	   
	   return mv;
   }
   
   
   //DB에 글 수정
   @RequestMapping(value="updatePro.do",method=RequestMethod.POST)
   public ModelAndView updatePro(BoardDto boardDto,String pageNum)
   throws IOException,NamingException{
	   sqlSession.update("board.updatePro",boardDto);//�ۼ���
	   
	  ModelAndView mv=new ModelAndView();
	  mv.addObject("pageNum", pageNum);
	  mv.setViewName("redirect:list.do");
	  return mv;
   }//
   
   //DB에 글 삭제
   @RequestMapping("delete.do")
   public String delete(Model model,String num, String pageNum) {
	   sqlSession.delete("board.deletePro",new Integer(num));
	   model.addAttribute("pageNum",pageNum);
	   return "redirect:list.do";
   }
   
}//class end