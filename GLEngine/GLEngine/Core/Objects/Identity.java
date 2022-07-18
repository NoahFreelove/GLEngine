package GLEngine.Core.Objects;

public final class Identity {
    String name;
    String tag;

    public Identity(String name, String tag){
        this.name = name;
        this.tag = tag;
    }

    public String getName(){
        return name;
    }

    public String getTag(){
        return tag;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setTag(String tag){
        this.tag = tag;
    }

    public boolean equals(Object obj){
        if(obj instanceof Identity){
            return ((Identity)obj).name.equals(name) && ((Identity)obj).tag.equals(tag);
        }
        return false;
    }

    public boolean compareTag(String tag){
        return this.tag.equals(tag);
    }

    public boolean compareName(String name){
        return this.name.equals(name);
    }
}
