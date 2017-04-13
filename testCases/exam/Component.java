public interface Component {
	public void printDescription() ;
	public void addChild(Component c);
	public void removeChild(Component c);
	public Component getChild(int i);
}