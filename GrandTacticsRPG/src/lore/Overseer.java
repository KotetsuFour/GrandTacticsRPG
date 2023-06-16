package lore;

import unit.human.Human;

public abstract class Overseer {

	private String name;
	private Human favorite;
	
	public boolean checkOpinionOfRuler(Human h) {
		if (h != null && h.isAlive()) {
			return false;
		}
		boolean ret = isFavorite(h);
		if (ret) {
			this.favorite = h;
			bless();
		}
		return ret;
	}
	
	public String getName() {
		return name;
	}
	
	/**
	 * Given a nation ruler, determine whether or not they excel in this Overseer's
	 * jurisdiction.
	 * @param h
	 * @return
	 */
	protected abstract boolean isFavorite(Human h);
	
	/**
	 * Buffs the favorite ruler according to the Overseer's jurisdiction
	 */
	protected abstract void bless();
	
	private class OverseerOfSpace extends Overseer {

		@Override
		protected boolean isFavorite(Human h) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		protected void bless() {
			// TODO Auto-generated method stub
			
		}
		
	}
	private class OverseerOfLight extends Overseer {

		@Override
		protected boolean isFavorite(Human h) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		protected void bless() {
			// TODO setup the favorite's nation so that the next person recruited will
			//be a Light Magic Champion (with high proficiency in light magic,
			//a high magic growth, and high resistance)
		}
		
	}
	private class OverseerOfElements extends Overseer {

		@Override
		protected boolean isFavorite(Human h) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		protected void bless() {
			// TODO setup the favorite's nation so that the next person recruited will
			//be an Anima Magic Champion (with high proficiency in anima magic,
			//a high magic growth, and high resistance)
			
		}
		
	}
	private class OverseerOfTime extends Overseer {

		@Override
		protected boolean isFavorite(Human h) {
			//TODO
			return false;
		}

		@Override
		protected void bless() {
			//TODO
		}
		
	}
	private class OverseerOfRealityPerception extends Overseer {

		@Override
		protected boolean isFavorite(Human h) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		protected void bless() {
			// TODO Auto-generated method stub
			
		}
		
	}
	private class OverseerOfDark extends Overseer {

		@Override
		protected boolean isFavorite(Human h) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		protected void bless() {
			// TODO setup the favorite's nation so that the next person recruited will
			//be a Dark Magic Champion (with high proficiency in dark magic,
			//a high magic growth, and high resistance)
		}
		
	}
	private class OverseerOfLuck extends Overseer {

		@Override
		protected boolean isFavorite(Human h) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		protected void bless() {
			// TODO Auto-generated method stub
			
		}
		
	}
	private class OverseerOfWar extends Overseer {

		@Override
		protected boolean isFavorite(Human h) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		protected void bless() {
			// TODO Auto-generated method stub
			
		}
		
	}
	private class OverseerOfPeace extends Overseer {

		@Override
		protected boolean isFavorite(Human h) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		protected void bless() {
			// TODO Auto-generated method stub
			
		}
		
	}
	private class OverseerOfConnection extends Overseer {

		@Override
		protected boolean isFavorite(Human h) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		protected void bless() {
			// TODO Auto-generated method stub
			
		}
		
	}
	private class OverseerOfLife extends Overseer {

		@Override
		protected boolean isFavorite(Human h) {
			//Right now, the average life span of a human unit is around 67.
			//There is about a one percent chance that a human will survive until 90
			return h.getAge() >= 90; 
		}

		@Override
		protected void bless() {
			if (favorite.isMortal()) {
				favorite.toggleMortality();
				int[] growths = favorite.getBodyPartsMaximumHPGrowth();
				//TODO increase growths (restore unit to youth, but leave age the same)
			}
		}
		
	}
	private class OverseerOfDeath extends Overseer {

		@Override
		protected boolean isFavorite(Human h) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		protected void bless() {
			// TODO Auto-generated method stub
			
		}
		
	}
}
