package isel.ps.employbox.model.binder.utils;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;


public class HalBuilder {
    public static <T extends ResourceSupport> T[] buildHalCollection(T [] ret){
        for(int i = 0; ret.length != 1 && i < ret.length; i++){
            if(i != 0)
                ret[i].add(new Link(ret[i-1].getId().getHref()).withRel(Link.REL_PREVIOUS));

            if(i!= ret.length-1)
                ret[i].add(new Link(ret[i+1].getId()
                        .getHref())
                        .withRel(Link.REL_NEXT));

            ret[i].add(new Link(ret[0]
                    .getId()
                    .getHref()).withRel(Link.REL_FIRST));

            ret[i].add(new Link(ret[ret.length-1]
                    .getId()
                    .getHref()).withRel(Link.REL_LAST));
        }
        return ret;
    }
}
