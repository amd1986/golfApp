package fr.greta.golf.services;

import fr.greta.golf.entities.Rule;
import fr.greta.golf.entities.Section;
import fr.greta.golf.entities.SubSection;
import org.docx4j.XmlUtils;
import org.docx4j.convert.in.xhtml.XHTMLImporterImpl;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.openpackaging.parts.WordprocessingML.NumberingDefinitionsPart;
import org.docx4j.wml.*;
import org.jsoup.Jsoup;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.bind.JAXBException;
import java.math.BigInteger;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <b>GenerateRuleDocServiceImpl est la classe implémentant IGenerateRuleDocService pour générer un document de règles locales</b>
 * <p>
 *     Cette classe fournit la méthode permettant de générer un document Word de règles locales
 * </p>
 *
 * @see IGenerateRuleDocService
 *
 * @author ahmed
 * @version 1.1.0
 */
public class GenerateRuleDocServiceImpl implements IGenerateRuleDocService {

    /**
     * Méthode generateDocxFromHtml.
     * <p>
     *     Méthode qui va générer un document de règles locales.
     * </p>
     *
     * @param sectionSet Set de catégories
     * @param subSectionSet Set de sous-catégories
     * @param ruleSet Set de règles de golf
     *
     * @throws Docx4JException Docx4j exception
     * @throws JAXBException JAXB exception
     */
    @Override @Transactional
    public WordprocessingMLPackage generateDocxFromHtml(Set<Section> sectionSet, Set<SubSection> subSectionSet, Set<Rule> ruleSet) throws Docx4JException, JAXBException {
        if (sectionSet != null) {

            List<Section> sectionsSorted = sectionSet.stream().sorted(Comparator.comparing(Section::getCode)).collect(Collectors.toList());
            List<SubSection> subSectionsSorted = subSectionSet.stream().sorted(Comparator.comparing(SubSection::getCode)).collect(Collectors.toList());
            List<Rule> rulesSorted = ruleSet.stream().sorted(Comparator.comparing(Rule::getCode)).collect(Collectors.toList());

            WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();

            NumberingDefinitionsPart ndp = new NumberingDefinitionsPart();
            wordMLPackage.getMainDocumentPart().addTargetPart(ndp);
            ndp.unmarshalDefaultNumbering();

            XHTMLImporterImpl xhtmlImporter = new XHTMLImporterImpl(wordMLPackage);
            MainDocumentPart main = wordMLPackage.getMainDocumentPart();
            main.addStyledParagraphOfText("Title", "Règles locales");
            ObjectFactory factory = Context.getWmlObjectFactory();

            RPr rpr = factory.createRPr();
            HpsMeasure size = new HpsMeasure();
            size.setVal(BigInteger.valueOf(30));
            rpr.setSz(size);

            for (Section section: sectionsSorted){
                P p = factory.createP();
                R r = factory.createR();
                r.setRPr(rpr);
                Text t = factory.createText();
                t.setValue(section.getCode()+ " - " +section.getTitle());
                r.getContent().add(t);
                p.getContent().add(r);
                main.getContent().add(p);
                System.out.println(XmlUtils.marshaltoString(p));

                for (SubSection sub: subSectionsSorted){
                    if (section.equals(sub.getSection())) {
                        P p1 = factory.createP();
                        R r1 = factory.createR();
                        Text t1 = factory.createText();
                        t1.setValue(sub.getCode() + " - " + sub.getTitle());
                        r1.getContent().add(t1);
                        r1.setRPr(rpr);
                        p1.getContent().add(r1);
                        main.getContent().add(p1);

                        P pSub = factory.createP();
                        R rSub = factory.createR();
                        rSub.getContent().addAll(xhtmlImporter.convert(Jsoup.parseBodyFragment(sub.getDescription()).html(), null));
                        pSub.getContent().add(rSub);
                        main.getContent().add(pSub);
                        xhtmlImporter = new XHTMLImporterImpl(wordMLPackage);
                    }

                        for (Rule rule: rulesSorted){
                            if (sub.equals(rule.getSubSection()) && section.equals(rule.getSubSection().getSection())){
                                P p2 = factory.createP();
                                R r2 = factory.createR();
                                Text t2 = factory.createText();
                                t2.setValue(rule.getCode()+ " - " +rule.getTitle());
                                r2.getContent().add(t2);
                                r2.setRPr(rpr);
                                p2.getContent().add(r2);
                                main.getContent().add(p2);


                                P pRule = factory.createP();
                                R rRule = factory.createR();
                                rRule.getContent().addAll(xhtmlImporter.convert(Jsoup.parseBodyFragment(rule.getText()).html(), null));
                                pRule.getContent().add(rRule);
                                main.getContent().add(pRule);
                                xhtmlImporter = new XHTMLImporterImpl(wordMLPackage);
                            }
                        }

                }
            }
            return wordMLPackage;
        }

        return null;
    }
}
