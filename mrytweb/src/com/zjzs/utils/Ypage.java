package com.zjzs.utils;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class Ypage extends SimpleTagSupport
{
  private int total;
  private int cpage;
  private int tsize;
  private int psize;

  public Ypage()
  {
    this.tsize = -1;
  }

  public void doTag() throws JspException, IOException
  {
    int i;
    JspWriter out = getJspContext().getOut();
    StringBuffer sb = new StringBuffer();
    String disable = "style=\"color: #DDDDDD;padding:3px;line-height:20px;\"";
    String cur = "style=\"border:1px solid #006696;background-color: #006696;color: #F0FFFF;\"";
    sb.append("<style type=\"text/css\">#ypagediv{width: 95%;margin: 0px auto;padding: 1px;overflow:hidden;}#ypagediv a{padding: 3px;color:#2A4B6E;text-decoration: none;font-size: 12px;} #ypagediv a:hover{color: #2A4B6E;} #ypagediv a:visited{color: #2A4B6E;}</style>");

    sb.append("<div id=\"ypagediv\">");
    if (this.cpage > this.total){
      this.cpage = this.total;
    }
    if (this.tsize > -1){
      sb.append("总共 <font color='red'>" + this.tsize + "</font> 条\n\r");
    }
    if (this.cpage > 1){
      sb.append("<a " + href(this.cpage - 1) + ">上一页</a>\n\r");
    }else{
      sb.append("<a " + disable + ">上一页</a>\n\r");
    }
    if (this.total <= 5){
      for (i = 1; i <= this.total; ++i)
        if (i == this.cpage){
          sb.append("<a " + href(this.cpage) + " " + cur + ">" + i + "</a>\n\r");
        }else{
          sb.append("<a " + href(i) + ">" + i + "</a>\n\r");
        }
    }else if (this.cpage < 5) {
      for (i = 1; i < 5; ++i){
        if (i == this.cpage){
          sb.append("<a " + href(this.cpage) + " " + cur + ">" + i + "</a>\n\r");
        }else{
          sb.append("<a " + href(i) + ">" + i + "</a>\n\r");
        }
      }
      sb.append("<a>...</a>\n\r");
      sb.append("<a " + href(this.total - 1) + ">" + (this.total - 1) + "</a>\n\r");
      sb.append("<a " + href(this.total) + ">" + this.total + "</a>\n\r");
    } else if (this.cpage == this.total - 3) {
      sb.append("<a " + href(1) + ">" + 1 + "</a>\n\r");
      sb.append("<a " + href(2) + ">" + 2 + "</a>\n\r");
      sb.append("<a>...</a>\n\r");
      for (i = this.total - 4; i < this.total; ++i) {
        if (i + 1 == this.cpage) {
          sb.append("<a " + href(i) + ">" + i + "</a>\n\r");
          sb.append("<a " + href(this.cpage) + " " + href(this.cpage) + " " + cur + ">" + this.cpage + "</a>\n\r");
          ++i;
          if (this.cpage == this.total)
            break;
        }

        sb.append("<a " + href(i + 1) + ">" + (i + 1) + "</a>\n\r");
      }
    } else if (this.cpage > this.total - 4) {
      sb.append("<a " + href(1) + ">" + 1 + "</a>\n\r");
      sb.append("<a " + href(2) + ">" + 2 + "</a>\n\r");
      sb.append("<a>...</a>\n\r");
      for (i = this.total - 4; i < this.total; ++i) {
        if (i + 1 == this.cpage) {
          sb.append("<a " + href(this.cpage) + " " + cur + ">" + this.cpage + "</a>\n\r");
          ++i;
          if (this.cpage == this.total)
            break;
        }
        sb.append("<a " + href(i + 1) + ">" + (i + 1) + "</a>\n\r");
      }
    } else {
      sb.append("<a " + href(1) + ">" + 1 + "</a>\n\r");
      sb.append("<a " + href(2) + ">" + 2 + "</a>\n\r");
      sb.append("<a>...</a>\n\r");
      sb.append("<a " + href(this.cpage - 1) + ">" + (this.cpage - 1) + "</a>\n\r");
      sb.append("<a " + href(this.cpage) + " " + cur + ">" + this.cpage + "</a>\n\r");
      sb.append("<a " + href(this.cpage + 1) + ">" + (this.cpage + 1) + "</a>\n\r");
      sb.append("<a>...</a>\n\r");
      sb.append("<a " + href(this.total - 1) + ">" + (this.total - 1) + "</a>\n\r");
      sb.append("<a " + href(this.total) + ">" + this.total + "</a>\n\r");
    }
    if (this.cpage != this.total)
      sb.append("<a " + href(this.cpage + 1) + ">下一页</a>\n\r");
    else {
      sb.append("<a " + disable + ">下一页</a>\n\r");
    }
    sb.append("去第<input type=\"text\"  id=\"ygopage\" style='width:30px'>页");
    sb.append("<button style=\"margin: 0px;\" onclick=\"if(ygopage.value==parseInt(ygopage.value)&&parseInt(ygopage.value)>0){if(parseInt(ygopage.value)>"+this.total+"){gopage("+this.total+")}else{gopage(parseInt(ygopage.value))}}else{alert('请输入正整数！！')};\">确定</button>");

    sb.append("</div>");

    out.write(sb.toString()); }

  public String href(int page) {
    return "href=\"javascript:gopage('" + page + "');\""; }

  public int getTotal() {
    return this.total; }

  public void setTotal(int total) {
    this.total = total; }

  public int getCpage() {
    return this.cpage; }

  public void setCpage(int cpage) {
    this.cpage = cpage; }

  public int getTsize() {
    return this.tsize; }

  public void setTsize(int tsize) {
    this.tsize = tsize;
    this.psize = ((this.psize == 0) ? 18 : this.psize);
    this.total = ((this.tsize + this.psize - 1) / this.psize);
    this.cpage = ((this.cpage > this.total) ? this.total : this.cpage); }

  public static void main(String[] args) throws JspException, IOException {
    Ypage y = new Ypage();
    y.setTotal(6);
    for (int i = 1; i <= 1000; ++i) {
      y.setCpage(i);
      y.doTag(); }
  }

  public int getPsize() {
    return this.psize; }

  public void setPsize(int psize) {
    this.psize = psize;
  }
}