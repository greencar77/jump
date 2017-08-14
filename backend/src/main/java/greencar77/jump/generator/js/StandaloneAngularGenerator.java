package greencar77.jump.generator.js;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.Validate;

import greencar77.jump.generator.Generator;
import greencar77.jump.model.js.AngularAppModel;
import greencar77.jump.model.js.AngularVersion;
import greencar77.jump.model.angular.Module;
import greencar77.jump.model.angular.controller.Controller;
import greencar77.jump.model.angular.controller.PaletteController;
import greencar77.jump.model.angular.controller.TemplateController;
import greencar77.jump.model.angular.directive.Directive;
import greencar77.jump.model.angular.html.Br;
import greencar77.jump.model.angular.html.DomNode;
import greencar77.jump.model.angular.html.HtmlFragment;
import greencar77.jump.model.angular.html.PaletteHtmlFragment;
import greencar77.jump.model.angular.html.TemplateHtmlFragment;

public class StandaloneAngularGenerator<T> extends Generator<AngularAppModel> {

    public StandaloneAngularGenerator(String projectFolder, AngularAppModel model) {
        super(projectFolder, model);
    }

    @Override
    public void generate() {
        List<String> localScriptPaths = generateJs();

        byte[] index = getIndex(model.getModules().iterator().next(), localScriptPaths); //TODO assume only one module exists        
        saveResource("index.html", index);

        generateHtml();
    }
    
    protected void generateHtml() {

        if (!model.isInlineHtml()) {
            for (HtmlFragment html: getAllHtmlFragments()) {                
                StringBuilder htmlBytes;
                if (html instanceof PaletteHtmlFragment) {
                    htmlBytes = generateHtml((PaletteHtmlFragment) html);
                } else if (html instanceof TemplateHtmlFragment) {
                    htmlBytes = generateHtml((TemplateHtmlFragment) html);
                } else {
                    htmlBytes = generateHtml(html);
                }
                saveResource(html.getFullPath(), htmlBytes.toString().getBytes());
            }
        }
    }
    
    //div views + directive fragments
    private Set<HtmlFragment> getAllHtmlFragments() {
        Set<HtmlFragment> result = new HashSet<HtmlFragment>();
        
        result.addAll(model.getHtmlFragments());
        //TODO assume only one module exists
        Module mainModule = model.getModules().iterator().next();
        mainModule.getDirectives().stream().forEach(d -> {
            if (d.getHtml() != null) {
                result.add(d.getHtml());
            }
        });
        
        return result;
    }

    /**
     * Each controller in own file
     */
    protected List<String> generateJs() {
        List<String> result = new ArrayList<>();
        
        //TODO assume only one module exists
        Module mainModule = model.getModules().iterator().next();

        {
            StringJoiner libs = new StringJoiner(",", "[", "]");
            if (model.isNgRoute()) {
                libs.add("'ngRoute'");
            }
            if (model.isNgCookies()) {
                libs.add("'ngCookies'");
            }
            if (model.isBootstrapUi()) {
                libs.add("'ui.bootstrap'");
            }
            
            StringBuilder sb = new StringBuilder();
            sb.append("var mod1 = angular.module('" + mainModule.getName() + "', " + libs.toString() + ");" + LF);
            result.add("js/app.js");
            saveResource("js/app.js", sb.toString().getBytes());
        }
        
        mainModule.getControllers().stream().forEach(c -> {
            Validate.notNull(c.getName());
            StringBuilder sb = new StringBuilder();
            
            if (c instanceof PaletteController) {
                sb.append(new String(generateController((PaletteController) c)));
            } else if (c instanceof TemplateController) {
                sb.append(new String(generateController((TemplateController) c)));
            } else {
                sb.append(new String(generateController(c)));
            }
            
            result.add("controllers/" + c.getName() + ".js");
            saveResource("controllers/" + c.getName() + ".js", sb.toString().getBytes());
        });
        
        mainModule.getDirectives().stream().forEach(d -> {
            Validate.notNull(d.getNormalizedName());
            StringBuilder sb = new StringBuilder();
            
            sb.append(new String(generateDirective(d)));
            
            result.add("directives/" + d.getNormalizedName() + ".js");
            saveResource("directives/" + d.getNormalizedName() + ".js", sb.toString().getBytes());
        });

        if (model.isNgRoute()) {
            StringBuilder sb = new StringBuilder();
            sb.append(new String(generateRoute()));
            result.add("js/route" + ".js");
            saveResource("js/route" + ".js", sb.toString().getBytes());
        }
        
        return result;
    }
    
    protected byte[] generateRoute() {
        StringBuilder sb = new StringBuilder();

        sb.append(LF + "mod1.config(function($routeProvider) {" + LF);
        sb.append(TAB + "$routeProvider" + LF);
        model.getHtmlFragments().stream()
            .forEach(h -> {
                sb.append(TAB + ".when(\"/" + h.getFilename() + "\", {" + LF);
                sb.append(TAB + TAB + "templateUrl : \"" + h.getPath() + h.getFilename() + ".html\"" + LF);
                sb.append(TAB + "})" + LF);
            });
        sb.append(TAB + ";" + LF);
        sb.append("});" + LF);
        
        return sb.toString().getBytes();
    }
    
    protected byte[] getIndex(Module module, List<String> localScriptPaths) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("<!doctype html>" + LF);
        sb.append("<html ng-app=\"" + module.getName() + "\">" + LF);
        appendHead(sb, localScriptPaths);
        appendBody(sb);
        sb.append("</html>" + LF);
        
        return sb.toString().getBytes();
    }
    
    protected void appendHead(StringBuilder sb, List<String> localScriptPaths) {
        sb.append("<head>" + LF);       
        sb.append("<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />" + LF);
        sb.append("<title>" + "index" + "</title>" + LF);
        sb.append(TAB + "<!--third-party dependencies-->" + LF);
        setupThirdParty(sb);
        sb.append(TAB + "<!--local dependencies-->" + LF);
        if (!CollectionUtils.isEmpty(localScriptPaths)) {
            localScriptPaths.stream().forEach(p -> {
                sb.append(TAB + "<script src=\"" + p + "\"></script>" + LF);
            });
        } else {
            sb.append(TAB + "<script src=\"script.js\"></script>" + LF);
        }
        sb.append("</head>" + LF);
    }
    
    protected void appendBody(StringBuilder sb) {
        sb.append("<body ng-controller=\"MainCtrl\">" + LF);
        
        if (model.isInlineHtml()) {
            //https://stackoverflow.com/questions/16124767/using-inline-templates-in-angular
            for (HtmlFragment html: getAllHtmlFragments()) {
                StringBuilder htmlBytes;
                if (html instanceof PaletteHtmlFragment) {
                    htmlBytes = generateHtml((PaletteHtmlFragment) html);
                } else if (html instanceof TemplateHtmlFragment) {
                    htmlBytes = generateHtml((TemplateHtmlFragment) html);
                } else {
                    htmlBytes = generateHtml(html);
                }
                sb.append(TAB + "<script type=\"text/ng-template\" id=\"" + html.getPath() + html.getFilename() + ".html" + "\">" + LF); //TODO path?                
                sb.append(htmlBytes);
                sb.append(TAB + "</script>" + LF);                
            }
        }
        
        //add links to all pages
        for (HtmlFragment html: model.getHtmlFragments()) { //TODO sort
            if (html.isStandalonePage()) {
                sb.append(TAB + "<a href=\"" 
                        + (model.isNgRoute()? getRouteName(html.getFilename()) : html.getFilename() + ".html")
                        + "\">" + html.getFilename() + "</a>" + "<br/>" + LF);
            }
        }
        
        Validate.notNull(model.getTitle());
        sb.append(TAB + "<h1>" + model.getTitle() + "</h1>" + LF);
        sb.append(TAB + "<h1>{{aa}}</h1>" + LF);
        sb.append(TAB + "<div ng-view></div>" + LF);
        sb.append("</body>" + LF);
    }
    
    protected String getRouteName(String name) {
        //https://stackoverflow.com/questions/41211875/angularjs-1-6-0-latest-now-routes-not-working
        return AngularVersion.is16Plus(model.getAngularVersion())? "#!/" + name : "#" + name;
    }

    protected void setupThirdParty(StringBuilder sb) {
        setupAngular(sb);
        if (model.isJquery()) {
            sb.append("<script src=\"../jquery/jquery-3.1.0.js\"></script>" + LF);
        }
        if (model.isBootstrapCss()) {
            sb.append(TAB + "<link href=\"http://netdna.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css\" rel=\"stylesheet\"/>" + LF);
        }
    }

    protected void setupAngular(StringBuilder sb) {
        //sb.append("<script src=\"../angular/angular.js\"></script>" + LF);
        final String angularUrl;
        
        switch (model.getAngularVersion()) {
            case V_1_5_7: angularUrl = "https://ajax.googleapis.com/ajax/libs/angularjs/1.5.7/";
                break;
            case LATEST: angularUrl = "http://code.angularjs.org/snapshot/";
                break;
            default: angularUrl = "https://ajax.googleapis.com/ajax/libs/angularjs/1.5.7/";
                break;
        }
        
        sb.append(TAB + "<script src=\"" + angularUrl + "angular.min.js\"></script>" + LF);
        if (model.isNgRoute()) {
            sb.append(TAB + "<script src=\"" + angularUrl + "angular-route.js\"></script>" + LF);          
        }
        if (model.isNgCookies()) {
            sb.append(TAB + "<script src=\"" + angularUrl + "angular-cookies.js\"></script>" + LF);          
        }
        if (model.isBootstrapUi()) {
            sb.append(TAB + "<script src=\"http://angular-ui.github.io/bootstrap/ui-bootstrap-tpls-2.5.0.js\"></script>" + LF);          
        }
    }
    
    protected StringBuilder generateHtml(HtmlFragment html) {
        StringBuilder sb = new StringBuilder();

        sb.append("<div"
                + (html.getDefaultController() == null || !html.isInlineControllerReference()? "": " ng-controller=\"" + html.getDefaultController().getName() + "\"")
                + ">" + LF);
        sb.append(TAB + "<h2>{{aa}}</h2>" + LF);
        html.getDirectiveUsages().stream()
            .forEach(d -> {
                sb.append(TAB + "<" + d.getName() + "></" + d.getName() + ">" + LF);
            });
        
        if (html.getRootNode() != null) {
            appendNode(sb, html.getRootNode(), 0);
        }
        sb.append("</div>" + LF);

        return sb;
    }
    
    protected void appendNode(StringBuilder sb, DomNode node, int level) {
        String indent;
        if (level == 0) {
            indent = "";
        } else {
            indent = String.format("%" + level + "s", "").replace(' ', '\t');
        }
        
        if (node instanceof Br) { //always treat as void, https://www.w3.org/TR/html51/syntax.html#void-elements
            sb.append(indent + "<" + node.getName() + getAttributes(node) + "/>" + LF);
        } else {
            sb.append(indent + "<" + node.getName() + getAttributes(node) + ">");
            if (node.getValue() == null) {
                if (node.getNodes().size() > 0) {
                    sb.append(LF);
                    for (DomNode subNode: node.getNodes()) {
                        appendNode(sb, subNode, level + 1);
                    }
                    sb.append(LF);
                }
            } else {
                sb.append(node.getValue());
            }
            sb.append(indent + "</" + node.getName() + ">" + LF);
        }
    }
    
    protected String getAttributes(DomNode node) {
        String result = "";
        for (Map.Entry<String, String> entry: node.getAttributes().entrySet()) {
            result += " " + entry.getKey() + "=\"" + entry.getValue() + "\"";
        }
        
        return result;
    }
    
    protected StringBuilder generateHtml(PaletteHtmlFragment html) {
        StringBuilder sb = new StringBuilder();

        sb.append(new String(TEMPLATE_MANAGER.getFilledTemplate("templates/palette/" + "palette.html", (Map<String, String>) null)));

        return sb;
    }
    
    protected StringBuilder generateHtml(TemplateHtmlFragment html) {
        StringBuilder sb = new StringBuilder();

        sb.append(new String(TEMPLATE_MANAGER.getFilledTemplate("templates/" + html.getTemplate(), (Map<String, String>) null)));

        return sb;
    }
    
    protected byte[] generateController(Controller controller) {
        StringBuilder sb = new StringBuilder();

        sb.append(LF + "mod1.controller('" + controller.getName() + "', function("
                + String.join(", ", controller.getParameters())
                + ") {" + LF 
                //+ TAB + "$scope.aa = '" + controller.getName() + "';" + LF
                );
        sb.append(controller.getContent());
        sb.append("});" + LF);

        return sb.toString().getBytes();
    }
    
    protected byte[] generateDirective(Directive directive) {
        StringBuilder sb = new StringBuilder();

        sb.append(LF + "mod1.directive('" + directive.getNormalizedName() + "', function() {" + LF
                + TAB + "return {" + LF
                /*
     restrict: 'E',
    scope: {
      customerInfo: '=info'
    },
    templateUrl: 'my-customer-plus-vojta.html'
                 */
                + TAB + TAB + "templateUrl: '" + directive.getHtml().getPath() + directive.getHtml().getFilename() + ".html" + "'" + LF
                + TAB + "};" + LF
                + "});" + LF);

        return sb.toString().getBytes();
    }
    
    protected byte[] generateController(PaletteController controller) {
        StringBuilder sb = new StringBuilder();

        sb.append(new String(TEMPLATE_MANAGER.getFilledTemplate("templates/palette/" + PaletteController.TEMPLATE, (Map<String, String>) null)));

        return sb.toString().getBytes();
    }
    
    protected byte[] generateController(TemplateController controller) {
        StringBuilder sb = new StringBuilder();

        sb.append(new String(TEMPLATE_MANAGER.getFilledTemplate("templates/" + controller.getTemplate(), (Map<String, String>) null)));

        return sb.toString().getBytes();
    }

}
