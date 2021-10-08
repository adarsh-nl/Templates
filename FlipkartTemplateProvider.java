package urjanet.pull.template;

import java.util.Arrays;

import urjanet.keys.InputKeys;
import urjanet.pull.core.PageSpec;
import urjanet.pull.core.PullJobTemplate;
import urjanet.pull.web.AgentVersion;
import urjanet.pull.web.BasePageSpec;
import urjanet.pull.web.ClickableNavTarget;
import urjanet.pull.web.CustomRequestNavTarget;
import urjanet.pull.web.CustomRequestNavTarget.RequestType;
import urjanet.pull.web.ExpandableNavTarget;
import urjanet.pull.web.InputElement;
import urjanet.pull.web.NavAction;
import urjanet.pull.web.UrlNavTarget;
import urjanet.pull.web.VariableInputElement;
import urjanet.pull.web.WebEngine;
import urjanet.pull.web.WebPullJobTemplate;
import urjanet.pull.web.XmlTargetGroup;

public class FlipkartTemplateProvider implements TemplateProvider {	

	private static final String SITE_URL = "https://www.flipkart.com/";

	private static final String TEMPLATE_NAME = "Flipkart";

	@Override

	public PullJobTemplate getTemplate() {
		
		InputElement username = new VariableInputElement( "//span[contains(text(),'Enter Email/Mobile number')]//ancestor-or-self::div//input[@type='text']", InputKeys.LOGIN_ID.getValue() );
		InputElement password = new VariableInputElement( "//span[contains(text(),'Enter Password')]//ancestor-or-self::div//input[@type='password']", InputKeys.LOGIN_PASS.getValue() );
		
		PageSpec nullpointer = null;
		
		CustomRequestNavTarget ordersCutom = new CustomRequestNavTarget("https://www.flipkart.com/account/orders?link=home_orders", RequestType.GET);
		ordersCutom.setWaitForXPathDataTargetStringToAppear("//div[contains(text(),\\\"Your item has been delivered\\\")]");
		
		ClickableNavTarget Download = new ClickableNavTarget( nullpointer, "//span[contains(text(),'Download')]//ancestor-or-self::button");
		Download.setAction(NavAction.JS_CLICK);
		BasePageSpec BillDownload = new BasePageSpec(new XmlTargetGroup(Download,ordersCutom));
		
		ExpandableNavTarget Products = new ExpandableNavTarget( BillDownload, "//div[contains(text(),\"Your item has been delivered\")]");
		Products.setAction(NavAction.JS_CLICK);
		Products.setDoNotUseAbsoluteXPath(true);
		BasePageSpec Orders = new BasePageSpec(new XmlTargetGroup(Products));

		ClickableNavTarget MyOrder = new ClickableNavTarget( Orders, "//div[contains(text(),'Orders')]//ancestor::a");
		MyOrder.setAction(NavAction.JS_CLICK);
		MyOrder.setWaitForXPathDataTargetStringToAppear("//div[contains(text(),\"Your item has been delivered\")]",10000);
		
		BasePageSpec Order = new BasePageSpec(new XmlTargetGroup(MyOrder));
		
		ClickableNavTarget hover = new ClickableNavTarget( Order, "//div[contains(text(),'manoj')]");
		hover.setAction(NavAction.MOUSE_OVER);

		BasePageSpec Profile = new BasePageSpec(new XmlTargetGroup(hover));

		ClickableNavTarget loginSubmit = new ClickableNavTarget( Profile, "//span[text()='Login']//ancestor-or-self::button", Arrays.asList(username, password) );
		loginSubmit.setAction(NavAction.JS_CLICK);
		loginSubmit.setWaitForXPathDataTargetStringToAppear("//div[contains(text(),'manoj')]", 40000);

		BasePageSpec loginpage = new BasePageSpec(new XmlTargetGroup(loginSubmit));

		UrlNavTarget urlNavTarget = new UrlNavTarget(loginpage, SITE_URL);
		urlNavTarget.setRefreshResponseFromServer(true);
		
		WebPullJobTemplate wpjt = new WebPullJobTemplate(urlNavTarget, TEMPLATE_NAME, "$LastChangedBy$", "$LastChangedRevision$");
		wpjt.setWebEngine(WebEngine.SELENIUM);

        wpjt.setAgentVersion(AgentVersion.FIREFOX_42);

		return wpjt;

	}

}