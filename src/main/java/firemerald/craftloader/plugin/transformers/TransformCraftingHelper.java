package firemerald.craftloader.plugin.transformers;

import java.util.Iterator;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class TransformCraftingHelper extends CLTransformer
{
	public static final String loadRecipes = "loadRecipes";
	public static final TransformCraftingHelper INSTANCE = new TransformCraftingHelper();

	public TransformCraftingHelper()
	{
		super(false);
	}

	@Override
	public void transform(ClassNode classNode, String className)
	{
		Iterator<MethodNode> methods = classNode.methods.iterator();
		while (methods.hasNext())
		{
			MethodNode m = methods.next();
			if (m.name.equals(loadRecipes) && m.desc.equals("(Lnet/minecraftforge/fml/common/ModContainer;)Z"))
			{
				logger().debug("Patching " + loadRecipes);
				int size = m.instructions.size();
				for (int i = 0; i < size; i++)
				{
					AbstractInsnNode node = m.instructions.get(i);
					//ASTORE 1
					if (node instanceof VarInsnNode && node.getOpcode() == ASTORE && ((VarInsnNode) node).var == 1)
					{
						m.instructions.insertBefore(node, new InsnNode(DUP));
						m.instructions.insert(node, new MethodInsnNode(INVOKESTATIC, "firemerald/craftloader/CraftingLoader", "putContext", "(Lnet/minecraftforge/common/crafting/JsonContext;)V", false));
						break;
					}
				}
			}
		}
	}
}